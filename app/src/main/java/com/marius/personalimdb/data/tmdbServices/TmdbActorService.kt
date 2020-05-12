package com.marius.personalimdb.data.tmdbServices

import com.marius.personalimdb.data.model.Actor
import com.marius.personalimdb.data.response.ImagesResponse
import com.marius.personalimdb.data.response.MovieCreditsResponse
import com.marius.personalimdb.data.response.SearchPeopleResponse
import com.marius.personalimdb.data.response.TvShowCreditsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbActorService {
    @GET("person/{person_id}")
    fun getActorDetails(
        @Path("person_id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<Actor>

    @GET("person/{person_id}/images")
    fun getActorPosters(
        @Path("person_id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<ImagesResponse>

    @GET("person/{person_id}/movie_credits")
    fun getActorMovies(
        @Path("person_id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<MovieCreditsResponse>

    @GET("person/{person_id}/tv_credits")
    fun getActorTvShows(
        @Path("person_id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<TvShowCreditsResponse>

    @GET("person/popular")
    fun getPopularPeople(
        @Query("api_key") apiKey: String,
        @Query(value = "page") page: Int = 1
    ): Call<SearchPeopleResponse>
}