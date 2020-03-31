package com.marius.personalimdb.data.imdbServices

import com.marius.personalimdb.data.imdbServices.ImdbMovieServiceFactory.API_KEY
import com.marius.personalimdb.data.model.ImdbRating
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ImdbMovieService {
    @GET(".")
    fun getRating(
        @Query("i") id: String,
        @Query("apikey") apiKey: String = API_KEY
    ): Call<ImdbRating>
}