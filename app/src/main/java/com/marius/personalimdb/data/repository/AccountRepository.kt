package com.marius.personalimdb.data.repository

import android.util.Log
import com.google.gson.Gson
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.moviedatabase.retrofitApi.responses.SearchMovieResponse
import com.marius.personalimdb.data.Account
import com.marius.personalimdb.data.model.LoginRequest
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.data.model.RequestToken
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.data.repository.callbacks.MovieListDetails
import com.marius.personalimdb.data.repository.callbacks.TvShowListDetails
import com.marius.personalimdb.data.response.SearchTvShowResponse
import com.marius.personalimdb.helper.filterAll
import com.marius.personalimdb.helper.filterAllTvShows
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AccountRepository {

    /**
     * Callback : Success,ErrorMessage
     */
    fun logIn(
        user: String,
        password: String,
        requestToken: String,
        callback: (Boolean, String?) -> Unit
    ) {
        TmdbServiceFactory.tmdbAccountService.logIn(
            LoginRequest(user, password, requestToken),
            TmdbServiceFactory.API_KEY
        ).enqueue(object : Callback<RequestToken> {
            override fun onFailure(call: Call<RequestToken>, t: Throwable) {
                Log.d("Login", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<RequestToken>,
                response: Response<RequestToken>
            ) {
                if (response.code() == 200) {
                    callback(true, null)
                } else {
                    response.errorBody()?.let {
                        val gson = Gson()
                        val error = gson.fromJson(it.charStream(), RequestToken::class.java)
                        callback(false, error.message)
                    }
                }
            }
        })
    }

    /**
     * Gets a request token generated by TMDB
     */
    fun getRequestToken(callback: (String) -> Unit) {
        TmdbServiceFactory.tmdbAccountService.getRequestToken(TmdbServiceFactory.API_KEY)
            .enqueue(object : Callback<RequestToken> {
                override fun onFailure(call: Call<RequestToken>, t: Throwable) {
                    Log.d("Login", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<RequestToken>,
                    response: Response<RequestToken>
                ) {
                    response.body()?.let {
                        it.value?.let { token ->
                            callback(token)
                        }
                    }
                }

            })
    }

    /**
     * Gets this session Id
     */
    fun getSessionId(requestToken: String, callback: (String) -> Unit) {
        TmdbServiceFactory.tmdbAccountService.getSessionId(
            RequestToken(value = requestToken),
            TmdbServiceFactory.API_KEY
        ).enqueue(object : Callback<RequestToken> {
            override fun onFailure(call: Call<RequestToken>, t: Throwable) {
                Log.d("Login", t.localizedMessage)
            }

            override fun onResponse(call: Call<RequestToken>, response: Response<RequestToken>) {
                response.body()?.let {
                    it.sessionId?.let { sessionId ->
                        callback(sessionId)
                    }
                }
            }
        })
    }

    /**
     * The user's id
     */
    fun getAccountId(sessionId: String, callback: (Int) -> Unit) {
        TmdbServiceFactory.tmdbAccountService.getAccount(sessionId, TmdbServiceFactory.API_KEY)
            .enqueue(object : Callback<RequestToken> {
                override fun onFailure(call: Call<RequestToken>, t: Throwable) {
                    Log.d("Login", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<RequestToken>,
                    response: Response<RequestToken>
                ) {
                    response.body()?.let {
                        it.id?.let { id ->
                            callback(id)
                        }
                    }
                }

            })
    }

    /**
     * Gets the movies that are on the user's watchlist
     */
    fun getWatchlistMovies(sessionId: String, page: Int, callback: (MovieListDetails) -> Unit) {
        var movieList: List<Movie> = emptyList()
        var totalPages = 1

        Account.details.id?.let {
            TmdbServiceFactory.tmdbAccountService.getWatchListMovies(
                it,
                sessionId,
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
                        Log.d("DEBUG", call.request().toString())
                        Log.d("DEBUG", response.toString())
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
    }

    /**
     * Gets the TV Shows that are on the user's watchlist
     */
    fun getWatchlistTvShows(sessionId: String, page: Int, callback: (TvShowListDetails) -> Unit) {
        var tvShowList: List<TvShow> = emptyList()
        var totalPages = 1
        Account.details.id?.let {
            TmdbServiceFactory.tmdbAccountService.getWatchListTvShows(
                it,
                sessionId,
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
                        tvShowList = it.filterAllTvShows().toMutableList()
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
    }
}