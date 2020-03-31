package com.marius.moviedatabase.retrofitApi

import com.google.gson.Gson
import com.marius.personalimdb.data.Keys
import com.marius.personalimdb.data.tmdbServices.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TmdbServiceFactory {

    private const val BASE_API_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = Keys.tmdbApiKey

    val tmdbMovieService: TmdbMovieService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(OkHttpClient.Builder().cache(null).build())
            .build()
            .create(TmdbMovieService::class.java)
    }

    val tmdbtvShowService: TmdbTvShowService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(OkHttpClient.Builder().cache(null).build())
            .build()
            .create(TmdbTvShowService::class.java)
    }

    val tmdbActorService: TmdbActorService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(OkHttpClient.Builder().cache(null).build())
            .build()
            .create(TmdbActorService::class.java)
    }

    val tmdbSearchService: TmdbSearchService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(OkHttpClient.Builder().cache(null).build())
            .build()
            .create(TmdbSearchService::class.java)
    }

    val tmdbAccountService: TmdbAccountService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(OkHttpClient.Builder().cache(null).build())
            .build()
            .create(TmdbAccountService::class.java)
    }
}