package com.marius.personalimdb.data.repository

import android.util.Log
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.moviedatabase.retrofitApi.responses.GetCastResponse
import com.marius.personalimdb.data.model.*
import com.marius.personalimdb.data.repository.callbacks.TvShowDetails
import com.marius.personalimdb.data.repository.callbacks.TvShowListDetails
import com.marius.personalimdb.data.response.ImagesResponse
import com.marius.personalimdb.data.response.SearchTvShowResponse
import com.marius.personalimdb.data.response.VideoResponse
import com.marius.personalimdb.helper.filterAllTvShows
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

object TvShowRepository {

    /**
     * Gets the trending TV Shows according to TheMovieDb
     */
    fun getTrendingTvShows(page: Int, callback: (TvShowListDetails) -> Unit) {
        var tvShowList: List<TvShow> = emptyList()
        var totalPages = 1

        TmdbServiceFactory.tmdbtvShowService.getTrendingTvShows(
            TmdbServiceFactory.API_KEY,
            page
        ).enqueue(object : Callback<SearchTvShowResponse> {
            override fun onFailure(call: Call<SearchTvShowResponse>, t: Throwable) {
                Log.d("TvShow", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<SearchTvShowResponse>,
                response: Response<SearchTvShowResponse>
            ) {
                response.body()?.totalPages?.let { pages ->
                    totalPages = pages
                }
                response.body()?.results?.let {
                    tvShowList = it.filterAllTvShows()
                }
                callback(
                    TvShowListDetails(
                        tvShowList,
                        totalPages
                    )
                )
            }
        })
    }

    /**
     * Get TV Shows that have an episode airing this week
     */
    fun getAiringTvShows(page: Int, callback: (TvShowListDetails) -> Unit) {
        var tvShowList: List<TvShow> = emptyList()
        var totalPages = 1

        TmdbServiceFactory.tmdbtvShowService.getAiringTvShows(
            TmdbServiceFactory.API_KEY,
            page
        ).enqueue(object : Callback<SearchTvShowResponse> {
            override fun onFailure(call: Call<SearchTvShowResponse>, t: Throwable) {
                Log.d("TvShow", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<SearchTvShowResponse>,
                response: Response<SearchTvShowResponse>
            ) {
                response.body()?.totalPages?.let { pages ->
                    totalPages = pages
                }
                response.body()?.results?.let {
                    tvShowList = it.filterAllTvShows()
                }
                callback(
                    TvShowListDetails(
                        tvShowList,
                        totalPages
                    )
                )
            }
        })
    }

    /**
     *  Get Netflix shows that were released at most 90 days ago
     */
    fun getNetflixTvShows(page: Int, callback: (TvShowListDetails) -> Unit) {
        var tvShowList: List<TvShow> = emptyList()
        var totalPages = 1

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date1 = Date(System.currentTimeMillis())
        val date2 = Calendar.getInstance()
        date2.add(Calendar.DAY_OF_YEAR, -90)
        val firstDateString = formatter.format(date1)
        val secondDateString = formatter.format(date2.timeInMillis)

        TmdbServiceFactory.tmdbtvShowService.getNetflixShows(
            TmdbServiceFactory.API_KEY,
            secondDateString,
            firstDateString,
            page
        ).enqueue(object : Callback<SearchTvShowResponse> {
            override fun onFailure(call: Call<SearchTvShowResponse>, t: Throwable) {
                Log.d("TvShow", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<SearchTvShowResponse>,
                response: Response<SearchTvShowResponse>
            ) {
                response.body()?.totalPages?.let { pages ->
                    totalPages = pages
                }
                response.body()?.results?.let {
                    tvShowList = it.filterAllTvShows()
                }
                callback(
                    TvShowListDetails(
                        tvShowList,
                        totalPages
                    )
                )
            }
        })
    }

    /**
     * Gets the top rated TV Shows based on TheMovieDb score, and deletes the first entry because i do not consider that a TV Show
     */
    fun getTopTvShows(page: Int, callback: (TvShowListDetails) -> Unit) {
        var tvShowList: List<TvShow> = emptyList()
        var totalPages = 1

        TmdbServiceFactory.tmdbtvShowService.getTopRatedTvShows(TmdbServiceFactory.API_KEY, page)
            .enqueue(object : Callback<SearchTvShowResponse> {
                override fun onFailure(call: Call<SearchTvShowResponse>, t: Throwable) {
                    Log.d("TvShow", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<SearchTvShowResponse>,
                    response: Response<SearchTvShowResponse>
                ) {
                    response.body()?.totalPages?.let { pages ->
                        totalPages = pages
                    }
                    response.body()?.results?.let {
                        val filteredShows = it.filterAllTvShows().toMutableList()
                        //The #1 show is weird AF so i removed it from the list
                        filteredShows.remove(filteredShows.first())
                        tvShowList = filteredShows
                    }
                    callback(
                        TvShowListDetails(
                            tvShowList,
                            totalPages
                        )
                    )
                }
            })
    }

    /**
     * Checks whether or not the TV Show is in the watchlist
     */
    fun getWatchlistStatus(tvShowId: Int, session: String, callback: (Boolean) -> Unit) {
        TmdbServiceFactory.tmdbAccountService.getWatchListStatusTv(
            tvShowId, session, TmdbServiceFactory.API_KEY
        ).enqueue(object : Callback<WatchListRequest> {
            override fun onFailure(call: Call<WatchListRequest>, t: Throwable) {
                Log.d("MovieListDetails", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<WatchListRequest>,
                response: Response<WatchListRequest>
            ) {
                response.body()?.let {
                    it.watchlist?.let { status ->
                        callback(status)
                    }
                }
            }
        })
    }

    /**
     * Adds or removes TV Show from watchlist based on current status
     */
    fun changeWatchlistStatus(
        tvShowId: Int,
        status: Boolean,
        session: String,
        callback: () -> Unit
    ) {
        TmdbServiceFactory.tmdbAccountService.changeWatchListStatus(
            WatchListRequest(tvShowId, !status, "tv"), session,
            TmdbServiceFactory.API_KEY
        ).enqueue(object : Callback<WatchListResponse> {
            override fun onFailure(call: Call<WatchListResponse>, t: Throwable) {
                Log.d("MovieListDetails", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<WatchListResponse>,
                response: Response<WatchListResponse>
            ) {
                Log.d("DEBUG", call.request().toString())
                Log.d("DEBUG", response.toString())
                callback()
            }
        })
    }

    /**
     * Gets the details of the TV Show
     */
    fun getTvShowDetails(tvShowId: Int, callback: (TvShowDetails) -> Unit) {
        var tvShow: TvShow
        var imdbId = ""
        var releaseYear: String
        var firstEpisode: String
        var seasons = "N/A"
        var lastEpisode = "N/A"
        var episodes: String
        var wastedTime: String
        var episodeDuration = "N/A"
        var genres = ""
        TmdbServiceFactory.tmdbtvShowService.getTvShowDetails(tvShowId, TmdbServiceFactory.API_KEY)
            .enqueue(object : Callback<TvShow> {
                override fun onFailure(call: Call<TvShow>, t: Throwable) {
                    Log.d("TvShow", t.localizedMessage)
                }

                override fun onResponse(call: Call<TvShow>, response: Response<TvShow>) {
                    response.body()?.let {
                        tvShow = it
                        it.externals?.imdbId?.let { id ->
                            imdbId = id
                        }
                        val release = LocalDate.parse(
                            it.firstAirDate,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        )
                        releaseYear = "${release.year}"
                        firstEpisode = "${release.dayOfMonth} ${release.month} ${release.year}"
                        it.seasons?.let { nrOfSeasons ->
                            seasons = if (nrOfSeasons > 1) {
                                "$nrOfSeasons seasons"
                            } else {
                                "$nrOfSeasons season"
                            }
                        }
                        it.lastAirDate?.let {
                            val lastEpisodeDate = LocalDate.parse(
                                it,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            )
                            lastEpisode =
                                "${lastEpisodeDate.dayOfMonth} ${lastEpisodeDate.month} ${lastEpisodeDate.year}"
                        }
                        episodes = "${it.episodes} episodes"
                        wastedTime = getWastedTimeValue(it)
                        it.runtime?.let {
                            if (it.isNotEmpty())
                                episodeDuration = "${it.first()} minutes"
                        }
                        it.genres?.forEach {
                            genres += "${it.name},"
                        }
                        if (genres.isNotBlank()) {
                            genres = genres.substring(0, genres.length - 1)
                        }
                        callback(
                            TvShowDetails(
                                tvShow,
                                imdbId,
                                releaseYear,
                                firstEpisode,
                                seasons,
                                lastEpisode,
                                episodes,
                                wastedTime,
                                episodeDuration,
                                genres
                            )
                        )
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

    /**
     * Gets when the last episode of a TV show was aired
     */
    fun getTvShowLastAirDate(tvShowId: Int, callback: (String?) -> Unit) {
        TmdbServiceFactory.tmdbtvShowService.getTvShowDetails(tvShowId, TmdbServiceFactory.API_KEY)
            .enqueue(object : Callback<TvShow> {
                override fun onFailure(call: Call<TvShow>, t: Throwable) {
                    Log.d("TvShow", t.localizedMessage)
                }

                override fun onResponse(call: Call<TvShow>, response: Response<TvShow>) {
                    response.body()?.let {
                        callback(it.lastAirDate)
                    }
                }
            })
    }

    /**
     * Gets the TV Show's cast
     */
    fun getTvShowCast(tvShowId: Int, callback: (List<Actor>) -> Unit) {
        TmdbServiceFactory.tmdbtvShowService.getTvShowCast(tvShowId, TmdbServiceFactory.API_KEY)
            .enqueue(object : Callback<GetCastResponse> {
                override fun onFailure(call: Call<GetCastResponse>, t: Throwable) {
                    Log.d("MovieListDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<GetCastResponse>,
                    response: Response<GetCastResponse>
                ) {
                    response.body()?.results?.let {
                        callback(it)
                    }
                }

            })
    }

    /**
     * Gets the trailer of the movie if it is on youtube
     */
    fun getTvShowTrailer(tvShowId: Int, callback: (Video?) -> Unit) {
        TmdbServiceFactory.tmdbtvShowService.getTvShowVideos(tvShowId, TmdbServiceFactory.API_KEY)
            .enqueue(object : Callback<VideoResponse> {
                override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                    Log.d("MovieListDetails", t.localizedMessage)
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
                        callback(video)
                    }
                }
            })
    }

    /**
     * Gets the posters of the show
     */
    fun getBackdrops(tvShowId: Int, callback: (List<Photo>) -> Unit) {
        TmdbServiceFactory.tmdbtvShowService.getTvShowPosters(tvShowId, TmdbServiceFactory.API_KEY)
            .enqueue(object : Callback<ImagesResponse> {
                override fun onFailure(call: Call<ImagesResponse>, t: Throwable) {
                    Log.d("MovieListDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<ImagesResponse>,
                    response: Response<ImagesResponse>
                ) {
                    Log.d("asd", call.request().toString())
                    response.body()?.results?.let {
                        callback(it)
                    }
                }
            })
    }

    /**
     * Gets the similar Tv Shows
     */
    fun getSimilarTvShows(tvShowId: Int, callback: (List<TvShow>) -> Unit) {
        TmdbServiceFactory.tmdbtvShowService.getSimilarTvShows(
            tvShowId,
            TmdbServiceFactory.API_KEY
        ).enqueue(object : Callback<SearchTvShowResponse> {
            override fun onFailure(call: Call<SearchTvShowResponse>, t: Throwable) {
                Log.d("TvShow", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<SearchTvShowResponse>,
                response: Response<SearchTvShowResponse>
            ) {
                response.body()?.results?.let {
                    callback(it.filterAllTvShows())
                }
            }

        })
    }

    /**
     * Gets the recommended Tv Shows
     */
    fun getRecommendedTvShows(tvShowId: Int, callback: (List<TvShow>) -> Unit) {
        TmdbServiceFactory.tmdbtvShowService.getRecommendedTvShows(
            tvShowId,
            TmdbServiceFactory.API_KEY
        ).enqueue(object : Callback<SearchTvShowResponse> {
            override fun onFailure(call: Call<SearchTvShowResponse>, t: Throwable) {
                Log.d("TvShow", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<SearchTvShowResponse>,
                response: Response<SearchTvShowResponse>
            ) {
                response.body()?.results?.let {
                    callback(it.filterAllTvShows())
                }
            }
        })
    }
}