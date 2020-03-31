package com.marius.personalimdb.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.moviedatabase.retrofitApi.responses.GetCastResponse
import com.marius.moviedatabase.retrofitApi.responses.SearchMovieResponse
import com.marius.personalimdb.data.model.*
import com.marius.personalimdb.data.repository.callbacks.MovieDetails
import com.marius.personalimdb.data.repository.callbacks.MovieListDetails
import com.marius.personalimdb.data.response.ImagesResponse
import com.marius.personalimdb.data.response.VideoResponse
import com.marius.personalimdb.helper.filterAll
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

object MovieRepository {

    /**
     * Gets the trending movies according to TheMovieDb
     */
    fun getTrendingMovies(page: Int, callback: (MovieListDetails) -> Unit) {
        var movieList: List<Movie> = emptyList()
        var totalPages = 1

        TmdbServiceFactory.tmdbMovieService.getTrendingMovies(
            TmdbServiceFactory.API_KEY,
            page
        ).enqueue(object : Callback<SearchMovieResponse> {
            override fun onFailure(call: Call<SearchMovieResponse>, t: Throwable) {
                Log.d("MoviesFragment", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<SearchMovieResponse>,
                response: Response<SearchMovieResponse>
            ) {
                response.body()?.totalPages?.let { pages ->
                    totalPages = pages
                }
                response.body()?.results?.let {
                    movieList = it.filterAll()
                }
                callback(
                    MovieListDetails(
                        movieList,
                        totalPages
                    )
                )
            }
        })
    }

    /**
     * Gets the movies that will release in the next 30 days
     */
    fun getUpcomingMovies(page: Int, callback: (MovieListDetails) -> Unit) {
        var movieList: List<Movie> = emptyList()
        var totalPages = 1

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date1 = Date(System.currentTimeMillis())
        val date2 = Calendar.getInstance()
        date2.add(Calendar.DAY_OF_YEAR, 30)
        val firstDateString = formatter.format(date1)
        val secondDateString = formatter.format(date2.timeInMillis)
        TmdbServiceFactory.tmdbMovieService.getUpcomingMovies(
            TmdbServiceFactory.API_KEY,
            firstDateString,
            secondDateString, page
        ).enqueue(object : Callback<SearchMovieResponse> {
            override fun onFailure(call: Call<SearchMovieResponse>, t: Throwable) {
                Log.d("MoviesFragment", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<SearchMovieResponse>,
                response: Response<SearchMovieResponse>
            ) {
                response.body()?.totalPages?.let { pages ->
                    totalPages = pages
                }
                response.body()?.results?.let {
                    movieList = it.filterAll()
                }
                callback(
                    MovieListDetails(
                        movieList,
                        totalPages
                    )
                )
            }
        })
    }

    /**
     * Gets the top rated movies based on TheMovieDb score
     */
    fun getTopMovies(page: Int, callback: (MovieListDetails) -> Unit) {
        var movieList: List<Movie> = emptyList()
        var totalPages = 1


        TmdbServiceFactory.tmdbMovieService.getTopMovies(
            TmdbServiceFactory.API_KEY,
            page
        )
            .enqueue(object : Callback<SearchMovieResponse> {
                override fun onFailure(call: Call<SearchMovieResponse>, t: Throwable) {
                    Log.d("MoviesFragment", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<SearchMovieResponse>,
                    response: Response<SearchMovieResponse>
                ) {
                    response.body()?.totalPages?.let { pages ->
                        totalPages = pages
                    }
                    response.body()?.results?.let {
                        movieList = it.filterAll()
                    }
                    callback(
                        MovieListDetails(
                            movieList,
                            totalPages
                        )
                    )
                }
            })
    }

    /**
     * Gets the most popular movies that were released at most 90 days ago
     */
    fun getPopularMovies(page: Int, callback: (MovieListDetails) -> Unit) {
        var movieList: List<Movie> = emptyList()
        var totalPages = 1
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date1 = Date(System.currentTimeMillis())
        val date2 = Calendar.getInstance()
        date2.add(Calendar.DAY_OF_YEAR, -90)
        val firstDateString = formatter.format(date1)
        val secondDateString = formatter.format(date2.timeInMillis)

        TmdbServiceFactory.tmdbMovieService.getPopularMovies(
            TmdbServiceFactory.API_KEY,
            secondDateString,
            firstDateString,
            page
        ).enqueue(object : Callback<SearchMovieResponse> {
            override fun onFailure(call: Call<SearchMovieResponse>, t: Throwable) {
                Log.d("MoviesFragment", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<SearchMovieResponse>,
                response: Response<SearchMovieResponse>
            ) {
                response.body()?.totalPages?.let { pages ->
                    totalPages = pages
                }
                response.body()?.results?.let {
                    movieList = it.filterAll()
                }
                callback(
                    MovieListDetails(
                        movieList,
                        totalPages
                    )
                )
            }
        })

    }

    /**
     * Checks whether or not the movie is in the watchlist
     */
    fun getWatchlistStatus(movieId: Int, session: String, callback: (Boolean) -> Unit) {
        TmdbServiceFactory.tmdbAccountService.getWatchListStatusMovie(
            movieId, session, TmdbServiceFactory.API_KEY
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
     * Adds or removes movie from watchlist based on current status
     */
    fun changeWatchlistStatus(
        movieId: Int,
        status: Boolean,
        session: String,
        callback: () -> Unit
    ) {
        TmdbServiceFactory.tmdbAccountService.changeWatchListStatus(
            WatchListRequest(movieId, !status, "movie"), session,
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
     * Gets the necessary movie details to show on screen
     */
    fun getMovieDetails(movieId: Int, callback: (MovieDetails) -> Unit) {
        var movie: Movie
        var imdbId = ""
        var releaseDate: String
        var duration: String
        var genres = ""
        TmdbServiceFactory.tmdbMovieService.getMovieDetails(movieId, TmdbServiceFactory.API_KEY)
            .enqueue(object : Callback<Movie> {
                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    Log.d("MovieListDetails", t.localizedMessage)
                }

                @SuppressLint("NewApi")
                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    response.body()?.let {
                        movie = it
                        it.imdbId?.let {
                            imdbId = it
                        }
                        val release = LocalDate.parse(
                            it.releaseDate,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        )
                        releaseDate = "${release.dayOfMonth} ${release.month} ${release.year}"
                        /* Duration in hours + minutes */
                        duration = "${it.duration / 60}h"
                        if (it.duration % 60 > 0)
                            duration += " ${it.duration % 60}m"
                        /*Extract string of all genres from the response*/
                        it.genres?.forEach {
                            genres += "${it.name},"
                        }
                        /* Delete last comma */
                        if (genres.isNotBlank()) {
                            genres = genres.substring(0, genres.length - 1)
                        }
                        callback(
                            MovieDetails(
                                movie,
                                imdbId,
                                releaseDate,
                                duration,
                                genres
                            )
                        )
                    }
                }

            })
    }

    /**
     * Gets the movie trailer
     */
    fun getMovieTrailer(movieId: Int, callback: (Video) -> Unit) {
        TmdbServiceFactory.tmdbMovieService.getMovieVideos(movieId, TmdbServiceFactory.API_KEY)
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
                        }.firstOrNull {
                            it.type == "Trailer"
                        }
                        video?.let { vid ->
                            callback(vid)
                        }
                    }
                }
            })
    }

    /**
     * Get the movie poster backdrops
     */
    fun getBackdrops(movieId: Int, callback: (List<Photo>) -> Unit) {
        TmdbServiceFactory.tmdbMovieService.getMoviePosters(movieId, TmdbServiceFactory.API_KEY)
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
     * Gets the actors that played in the movie
     */
    fun getMovieCast(movieId: Int, callback: (List<Actor>) -> Unit) {
        TmdbServiceFactory.tmdbMovieService.getMovieCast(movieId, TmdbServiceFactory.API_KEY)
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
     * Movie recommendations based on this movie
     */
    fun getRecommendedMovies(movieId: Int, callback: (List<Movie>) -> Unit) {
        TmdbServiceFactory.tmdbMovieService.getRecommendedMovies(
            movieId,
            TmdbServiceFactory.API_KEY
        )
            .enqueue(object : Callback<SearchMovieResponse> {
                override fun onFailure(call: Call<SearchMovieResponse>, t: Throwable) {
                    Log.d("MovieListDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<SearchMovieResponse>,
                    response: Response<SearchMovieResponse>
                ) {
                    response.body()?.let {
                        it.results?.filterAll()?.let { movies ->
                            callback(movies)
                        }
                    }
                }
            })
    }

    /**
     * Similar movies to this one
     */
    fun getSimilarMovies(movieId: Int, callback: (List<Movie>) -> Unit) {
        TmdbServiceFactory.tmdbMovieService.getSimilarMovies(movieId, TmdbServiceFactory.API_KEY)
            .enqueue(object : Callback<SearchMovieResponse> {
                override fun onFailure(call: Call<SearchMovieResponse>, t: Throwable) {
                    Log.d("MovieListDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<SearchMovieResponse>,
                    response: Response<SearchMovieResponse>
                ) {
                    response.body()?.let {
                        it.results?.filterAll()?.let { movies ->
                            callback(movies)
                        }
                    }
                }
            })
    }
}