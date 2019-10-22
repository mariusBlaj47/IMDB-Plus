package com.marius.personalimdb.data.tmdbApi

import com.marius.moviedatabase.retrofitApi.responses.GetCastResponse
import com.marius.moviedatabase.retrofitApi.responses.SearchMovieResponse
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.data.response.ImagesResponse
import com.marius.personalimdb.data.response.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbMovieService {

    @GET("search/movie")
    fun searchMovies(
        @Query("query") queryText: String,
        @Query("api_key") apiKey: String,
        @Query(value = "page") page: Int = 1
    ): Call<SearchMovieResponse>

    @GET("movie/{id}/credits")
    fun getMovieCast(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<GetCastResponse>

    @GET("movie/top_rated")
    fun getTopMovies(
        @Query("api_key") apiKey: String,
        @Query(value = "page") page: Int = 1
    ): Call<SearchMovieResponse>

    @GET("discover/movie")
    fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("primary_release_date.gte") firstDate: String,
        @Query("primary_release_date.lte") lastDate: String,
        @Query(value = "page") page: Int = 1,
        @Query("include_adult") adultContent: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("sort_by") sortValue: String = "vote_average.desc",
        @Query("vote_count.gte") votes: String = "1000",
        @Query("language") language: String = "en-US",
        @Query("with_original_language") language2: String = "en"
    ): Call<SearchMovieResponse>

    @GET("discover/movie")
    fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("primary_release_date.gte") firstDate: String,
        @Query("primary_release_date.lte") lastDate: String,
        @Query(value = "page") page: Int = 1,
        @Query("include_adult") adultContent: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("sort_by") sortValue: String = "popularity.desc",
        @Query("language") language: String = "en-US",
        @Query("with_original_language") language2: String = "en"
    ): Call<SearchMovieResponse>

    @GET("trending/movie/day")
    fun getTrendingMovies(
        @Query("api_key") apiKey: String,
        @Query(value = "page") page: Int = 1
    ): Call<SearchMovieResponse>

    @GET("movie/{movie_id}/similar")
    fun getSimilarMovies(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<SearchMovieResponse>

    @GET("movie/{movie_id}/recommendations")
    fun getRecommendedMovies(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<SearchMovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<Movie>

    @GET("movie/{movie_id}/videos")
    fun getMovieVideos(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<VideoResponse>

    @GET("movie/{movie_id}/images")
    fun getMoviePosters(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<ImagesResponse>


}
