package com.marius.personalimdb.data.tmdbServices

import com.marius.moviedatabase.retrofitApi.responses.SearchMovieResponse
import com.marius.personalimdb.data.model.LoginRequest
import com.marius.personalimdb.data.model.RequestToken
import com.marius.personalimdb.data.model.WatchListRequest
import com.marius.personalimdb.data.model.WatchListResponse
import com.marius.personalimdb.data.response.SearchTvShowResponse
import retrofit2.Call
import retrofit2.http.*

interface TmdbAccountService {
    @GET("authentication/token/new")
    fun getRequestToken(
        @Query("api_key") apiKey: String
    ): Call<RequestToken>

    @POST("authentication/token/validate_with_login")
    fun logIn(
        @Body request: LoginRequest,
        @Query("api_key") apiKey: String
    ): Call<RequestToken>

    @POST("authentication/session/new")
    fun getSessionId(
        @Body request: RequestToken,
        @Query("api_key") apiKey: String
    ): Call<RequestToken>

    @GET("account")
    fun getAccount(
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String
    ): Call<RequestToken>

    @POST("account/{account_id}/watchlist")
    fun changeWatchListStatus(
        @Body request: WatchListRequest,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String
    ): Call<WatchListResponse>

    @GET("account/{account_id}/watchlist/movies")
    fun getWatchListMovies(
        @Path("account_id") acc_id: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("sort_by") sortValue: String = "created_at.desc"
    ): Call<SearchMovieResponse>

    @GET("account/{account_id}/watchlist/tv")
    fun getWatchListTvShows(
        @Path("account_id") acc_id: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("sort_by") sortValue: String = "created_at.desc"
    ): Call<SearchTvShowResponse>

    @GET("movie/{movie_id}/account_states")
    fun getWatchListStatusMovie(
        @Path("movie_id") movieId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String
    ): Call<WatchListRequest>


    @GET("tv/{tv_id}/account_states")
    fun getWatchListStatusTv(
        @Path("tv_id") movieId: Int,
        @Query("session_id") sessionId: String,
        @Query("api_key") apiKey: String
    ): Call<WatchListRequest>
}