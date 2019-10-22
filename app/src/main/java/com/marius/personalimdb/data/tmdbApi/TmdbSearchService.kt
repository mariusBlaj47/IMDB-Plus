package com.marius.personalimdb.data.tmdbApi

import com.marius.personalimdb.data.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbSearchService {
    @GET("search/multi")
    fun searchContent(
        @Query("query") queryText: String,
        @Query("api_key") apiKey: String,
        @Query(value = "page") page: Int = 1,
        @Query("include_adult") adultContent: Boolean = false
    ): Call<SearchResponse>
}