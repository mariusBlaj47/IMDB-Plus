package com.marius.personalimdb.ui.movies.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory.API_KEY
import com.marius.moviedatabase.retrofitApi.responses.GetCastResponse
import com.marius.moviedatabase.retrofitApi.responses.SearchMovieResponse
import com.marius.personalimdb.data.Account
import com.marius.personalimdb.data.imdbApi.ImdbMovieServiceFactory
import com.marius.personalimdb.data.model.*
import com.marius.personalimdb.data.response.ImagesResponse
import com.marius.personalimdb.data.response.VideoResponse
import com.marius.personalimdb.helper.filterAll
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsViewModel : ViewModel() {
    val movie = MutableLiveData<Movie>()
    val rating = MutableLiveData<ImdbRating>(ImdbRating("-", "-"))
    val releaseDate = MutableLiveData<String>()
    val duration = MutableLiveData<String>()
    val genres = MutableLiveData<String>("")
    val similarMovies = MutableLiveData(mutableListOf<Movie>())
    val recommendedMovies = MutableLiveData(mutableListOf<Movie>())
    val actorList = MutableLiveData(mutableListOf<Actor>())
    val trailer = MutableLiveData<Video>()
    val posters = MutableLiveData(mutableListOf<Media>())
    val startImdb = MutableLiveData<Unit>()
    val isInWatchlist = MutableLiveData<Boolean>(false)

    fun getData(movieId: Int) {
        getDetails(movieId)
        checkWatchlist(movieId)
        getSimilar(movieId)
        getRecommended(movieId)
        getCast(movieId)
        getTrailer(movieId)
    }

    fun changeWatchListStatus() {
        isInWatchlist.value?.let { status ->
            Account.details.sessionId?.let { session ->
                TmdbServiceFactory.tmdbAccountService.changeWatchListStatus(
                    WatchListRequest(movie.value?.id, !status, "movie"), session, API_KEY
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

    private fun checkWatchlist(movieId: Int) {
        Account.details.sessionId?.let { session ->
            TmdbServiceFactory.tmdbAccountService.getWatchListStatusMovie(
                movieId, session, API_KEY
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

    private fun getTrailer(movieId: Int) {
        TmdbServiceFactory.tmdbMovieService.getMovieVideos(movieId, API_KEY)
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
                        }.firstOrNull {
                            it.type == "Trailer"
                        }
                        video?.let {
                            trailer.value = video
                        }
                    }
                    getPhotos(movieId)
                }
            })
    }

    private fun getPhotos(movieId: Int) {
        TmdbServiceFactory.tmdbMovieService.getMoviePosters(movieId, API_KEY)
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

    private fun getCast(movieId: Int) {
        TmdbServiceFactory.tmdbMovieService.getMovieCast(movieId, API_KEY)
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

    private fun getRecommended(movieId: Int) {
        TmdbServiceFactory.tmdbMovieService.getRecommendedMovies(movieId, API_KEY)
            .enqueue(object : Callback<SearchMovieResponse> {
                override fun onFailure(call: Call<SearchMovieResponse>, t: Throwable) {
                    Log.d("MovieDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<SearchMovieResponse>,
                    response: Response<SearchMovieResponse>
                ) {
                    response.body()?.let {
                        recommendedMovies.value?.clear()
                        recommendedMovies.value = it.results?.filterAll()?.toMutableList()
                    }
                }

            })
    }

    private fun getSimilar(movieId: Int) {
        TmdbServiceFactory.tmdbMovieService.getSimilarMovies(movieId, API_KEY)
            .enqueue(object : Callback<SearchMovieResponse> {
                override fun onFailure(call: Call<SearchMovieResponse>, t: Throwable) {
                    Log.d("MovieDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<SearchMovieResponse>,
                    response: Response<SearchMovieResponse>
                ) {
                    response.body()?.let {
                        similarMovies.value?.clear()
                        similarMovies.value = it.results?.filterAll()?.toMutableList()
                    }
                }

            })
    }

    private fun getDetails(movieId: Int) {
        TmdbServiceFactory.tmdbMovieService.getMovieDetails(movieId, API_KEY)
            .enqueue(object : Callback<Movie> {
                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    Log.d("MovieDetails", t.localizedMessage)
                }

                @SuppressLint("NewApi")
                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    response.body()?.let {
                        movie.value = it
                        it.imdbId?.let {
                            getRating(it)
                        }
                        val release = LocalDate.parse(
                            it.releaseDate,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        )
                        releaseDate.value = "${release.dayOfMonth} ${release.month} ${release.year}"
                        duration.value = "${it.duration / 60}h"
                        if (it.duration % 60 > 0)
                            duration.value += " ${it.duration % 60}m"
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
                        Log.d("MovieDetails", it.toString())
                    }
                    Log.d("MovieDetails", call.request().toString())
                }
            })
    }

    fun openImdb() {
        startImdb.value = Unit
    }

}