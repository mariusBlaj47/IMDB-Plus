package com.marius.personalimdb.ui.tvShows.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory.API_KEY
import com.marius.moviedatabase.retrofitApi.responses.GetCastResponse
import com.marius.personalimdb.data.Account
import com.marius.personalimdb.data.imdbApi.ImdbMovieServiceFactory
import com.marius.personalimdb.data.model.*
import com.marius.personalimdb.data.response.ImagesResponse
import com.marius.personalimdb.data.response.SearchTvShowResponse
import com.marius.personalimdb.data.response.VideoResponse
import com.marius.personalimdb.helper.filterAllTvShows
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowDetailsViewModel : ViewModel() {
    val tvShow = MutableLiveData<TvShow>()
    val releaseYear = MutableLiveData<String>()
    val firstEpisode = MutableLiveData<String>("-")
    val lastEpisode = MutableLiveData<String>("-")
    val seasons2 = MutableLiveData<String>("-")
    val episodes = MutableLiveData<String>("-")
    val wastedTime = MutableLiveData<String>("-")
    val seasons = MutableLiveData<String>()
    val duration = MutableLiveData<String>("-")
    val genres = MutableLiveData<String>("")
    val trailer = MutableLiveData<Video>()
    val posters = MutableLiveData(mutableListOf<Media>())
    val actorList = MutableLiveData(mutableListOf<Actor>())
    val similarTvShows = MutableLiveData(mutableListOf<TvShow>())
    val recommendedTvShows = MutableLiveData(mutableListOf<TvShow>())
    val rating = MutableLiveData<ImdbRating>(ImdbRating("-", "-"))
    val startImdb = MutableLiveData<Unit>()
    val isInWatchlist = MutableLiveData<Boolean>(false)

    fun getData(tvShowId: Int) {
        getDetails(tvShowId)
        checkWatchlist(tvShowId)
        getSimilar(tvShowId)
        getRecommended(tvShowId)
        getTrailer(tvShowId)
        getCast(tvShowId)
    }

    fun changeWatchListStatus() {
        isInWatchlist.value?.let { status ->
            Account.details.sessionId?.let { session ->
                TmdbServiceFactory.tmdbAccountService.changeWatchListStatus(
                    WatchListRequest(tvShow.value?.id, !status, "tv"), session, API_KEY
                ).enqueue(object : Callback<WatchListResponse> {
                    override fun onFailure(call: Call<WatchListResponse>, t: Throwable) {
                        Log.d("MovieDetails", t.localizedMessage)
                    }

                    override fun onResponse(
                        call: Call<WatchListResponse>,
                        response: Response<WatchListResponse>
                    ) {
                        isInWatchlist.value = !status
                    }
                })
            }

        }
    }

    private fun checkWatchlist(tvShowId: Int) {
        Account.details.sessionId?.let { session ->
            TmdbServiceFactory.tmdbAccountService.getWatchListStatusTv(
                tvShowId, session, API_KEY
            ).enqueue(object : Callback<WatchListRequest> {
                override fun onFailure(call: Call<WatchListRequest>, t: Throwable) {
                    Log.d("MovieDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<WatchListRequest>,
                    response: Response<WatchListRequest>
                ) {
                    response.body()?.let {
                        isInWatchlist.value = it.watchlist
                    }
                }
            })
        }
    }

    private fun getDetails(tvShowId: Int) {
        TmdbServiceFactory.tmdbtvShowService.getTvShowDetails(tvShowId, API_KEY)
            .enqueue(object : Callback<TvShow> {
                override fun onFailure(call: Call<TvShow>, t: Throwable) {
                    Log.d("TvShow", t.localizedMessage)
                }

                override fun onResponse(call: Call<TvShow>, response: Response<TvShow>) {
                    response.body()?.let {
                        tvShow.value = it
                        it.externals?.imdbId?.let {
                            getRating(it)
                        }
                        val release = LocalDate.parse(
                            it.firstAirDate,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        )
                        releaseYear.value = "${release.year}"
                        firstEpisode.value =
                            "${release.dayOfMonth} ${release.month} ${release.year}"
                        it.seasons?.let {
                            if (it > 1) {
                                seasons.value = "${it} seasons ● "
                                seasons2.value = "${it} seasons"
                            } else {
                                seasons.value = "${it} season ● "
                                seasons2.value = "${it} season"
                            }
                        }
                        it.lastAirDate?.let {
                            val lastEpisodeDate = LocalDate.parse(
                                it,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            )
                            lastEpisode.value =
                                "${lastEpisodeDate.dayOfMonth} ${lastEpisodeDate.month} ${lastEpisodeDate.year}"
                        }
                        episodes.value = "${it.episodes} episodes"
                        wastedTime.value = getWastedTimeValue(it)
                        it.runtime?.let {
                            if (it.isNotEmpty())
                                duration.value = "${it.first()} minutes"
                        }
                        it.genres?.forEach {
                            genres.value += "${it.name},"
                        }
                        genres.value?.let {
                            if (it.isNotBlank()) {
                                genres.value = genres.value?.length?.minus(1)?.let { it1 ->
                                    genres.value?.substring(
                                        0,
                                        it1
                                    )
                                }
                            }
                        }
                    }
                }

                private fun getWastedTimeValue(tvShow: TvShow): String {
                    var result = "-"
                    if (tvShow.episodes != null && tvShow.runtime != null && tvShow.runtime.isNotEmpty()) {
                        result = ""
                        var minutes = tvShow.episodes * tvShow.runtime.first()
                        val days = minutes / 1440
                        if (days > 0) {
                            result += if (days > 1) {
                                "$days days "
                            } else "$days day "
                        }
                        minutes %= 1440
                        val hours = minutes / 60
                        if (hours > 0) {
                            result += if (hours > 1) {
                                "$hours hours "
                            } else "$hours hour "
                        }
                        minutes %= 60
                        if (minutes > 0) {
                            result += if (minutes > 1)
                                "$minutes minutes"
                            else "$minutes minute"
                        }
                    }
                    return result
                }

            })
    }

    private fun getRating(imdbId: String) {
        Log.d("MovieDetails", "made it")
        ImdbMovieServiceFactory.tmdbMovieService.getRating(imdbId)
            .enqueue(object : Callback<ImdbRating> {
                override fun onFailure(call: Call<ImdbRating>, t: Throwable) {
                    Log.d("MovieDetails", t.localizedMessage)
                }

                override fun onResponse(call: Call<ImdbRating>, response: Response<ImdbRating>) {
                    response.body()?.let {
                        rating.value = it
                    }
                }
            })
    }

    private fun getCast(tvShowId: Int) {
        TmdbServiceFactory.tmdbtvShowService.getTvShowCast(tvShowId, API_KEY)
            .enqueue(object : Callback<GetCastResponse> {
                override fun onFailure(call: Call<GetCastResponse>, t: Throwable) {
                    Log.d("MovieDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<GetCastResponse>,
                    response: Response<GetCastResponse>
                ) {
                    response.body()?.results?.let {
                        actorList.value = it.toMutableList()
                    }
                }

            })
    }

    private fun getTrailer(tvShowId: Int) {
        TmdbServiceFactory.tmdbtvShowService.getTvShowVideos(tvShowId, API_KEY)
            .enqueue(object : Callback<VideoResponse> {
                override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                    Log.d("MovieDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<VideoResponse>,
                    response: Response<VideoResponse>
                ) {
                    response.body()?.videos?.let {
                        val video = it.filter {
                            it.site == "YouTube"
                        }.filter {
                            !it.name.contains("Comic-Con", true)
                        }.firstOrNull {
                            it.type == "Trailer"
                        }
                        video?.let {
                            trailer.value = video
                        }
                    }
                    getPhotos(tvShowId)
                }
            })
    }

    private fun getPhotos(tvShowId: Int) {
        TmdbServiceFactory.tmdbtvShowService.getTvShowPosters(tvShowId, API_KEY)
            .enqueue(object : Callback<ImagesResponse> {
                override fun onFailure(call: Call<ImagesResponse>, t: Throwable) {
                    Log.d("MovieDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<ImagesResponse>,
                    response: Response<ImagesResponse>
                ) {
                    Log.d("asd", call.request().toString())
                    response.body()?.results?.let {
                        posters.value = it.take(6).toMutableList()
                    }
                }

            })
    }

    private fun getSimilar(tvShowId: Int) {
        TmdbServiceFactory.tmdbtvShowService.getSimilarTvShows(tvShowId, API_KEY)
            .enqueue(object : Callback<SearchTvShowResponse> {
                override fun onFailure(call: Call<SearchTvShowResponse>, t: Throwable) {
                    Log.d("TvShow", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<SearchTvShowResponse>,
                    response: Response<SearchTvShowResponse>
                ) {
                    response.body()?.results?.let {
                        similarTvShows.value = it.filterAllTvShows().toMutableList()
                    }
                }

            })
    }

    private fun getRecommended(tvShowId: Int) {
        TmdbServiceFactory.tmdbtvShowService.getRecommendedTvShows(tvShowId, API_KEY)
            .enqueue(object : Callback<SearchTvShowResponse> {
                override fun onFailure(call: Call<SearchTvShowResponse>, t: Throwable) {
                    Log.d("TvShow", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<SearchTvShowResponse>,
                    response: Response<SearchTvShowResponse>
                ) {
                    response.body()?.results?.let {
                        recommendedTvShows.value = it.filterAllTvShows().toMutableList()
                    }
                }

            })
    }

    fun openImdb() {
        startImdb.value = Unit
    }
}