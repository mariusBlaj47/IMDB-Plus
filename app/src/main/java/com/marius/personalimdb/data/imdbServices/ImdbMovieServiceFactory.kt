package com.marius.personalimdb.data.imdbServices

import com.google.gson.Gson
import com.marius.personalimdb.data.Keys
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImdbMovieServiceFactory {
    private const val BASE_API_URL = "https://www.omdbapi.com/"
    const val API_KEY = Keys.omdbApiKey

    val tmdbMovieService: ImdbMovieService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(OkHttpClient.Builder().build())
            .build()
            .create(ImdbMovieService::class.java)
    }
}