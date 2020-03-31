package com.marius.personalimdb.data.tmdbServices

import com.marius.moviedatabase.retrofitApi.responses.GetCastResponse
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.data.response.ImagesResponse
import com.marius.personalimdb.data.response.SearchTvShowResponse
import com.marius.personalimdb.data.response.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbTvShowService {
    @GET("tv/top_rated")
    fun getTopRatedTvShows(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): Call<SearchTvShowResponse>

    @GET("trending/tv/day")
    fun getTrendingTvShows(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("include_adult") adultContent: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false
    ): Call<SearchTvShowResponse>


    @GET("tv/on_the_air")
    fun getAiringTvShows(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1,
        @Query("language") language: String = "en-US",
        @Query("include_adult") adultContent: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false
    ): Call<SearchTvShowResponse>


    @GET("discover/tv")
    fun getNetflixShows(
        @Query("api_key") apiKey: String,
        @Query("first_air_date.gte") firstDate: String,
        @Query("first_air_date.lte") lastDate: String,
        @Query("page") page: Int = 1,
        @Query("with_networks") networks: String = "213",
        @Query("include_null_first_air_dates") nulls: Boolean = false,
        @Query("sort_by") sortValue: String = "popularity.desc",
        @Query("language") language: String = "en-US",
        @Query("include_adult") adultContent: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false
    ): Call<SearchTvShowResponse>

    @GET("tv/{tv_id}")
    fun getTvShowDetails(
        @Path("tv_id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("append_to_response") params: String = "external_ids"
    ): Call<TvShow>

    @GET("tv/{tv_id}/videos")
    fun getTvShowVideos(
        @Path("tv_id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<VideoResponse>

    @GET("tv/{tv_id}/images")
    fun getTvShowPosters(
        @Path("tv_id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<ImagesResponse>

    @GET("tv/{tv_id}/credits")
    fun getTvShowCast(
        @Path("tv_id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<GetCastResponse>

    @GET("tv/{tv_id}/similar")
    fun getSimilarTvShows(
        @Path("tv_id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("include_adult") adultContent: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false
    ): Call<SearchTvShowResponse>


    @GET("tv/{tv_id}/recommendations")
    fun getRecommendedTvShows(
        @Path("tv_id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("include_adult") adultContent: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false
    ): Call<SearchTvShowResponse>
}