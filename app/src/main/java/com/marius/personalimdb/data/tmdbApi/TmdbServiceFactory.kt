package com.marius.moviedatabase.retrofitApi

import com.google.gson.Gson
import com.marius.personalimdb.data.tmdbApi.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TmdbServiceFactory {

    private const val BASE_API_URL = "https://api.themoviedb.org/3/"
    //TODO : Add API_KEY here
    const val API_KEY = ""

    val tmdbMovieService: TmdbMovieService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(OkHttpClient.Builder().build())
            .build()
            .create(TmdbMovieService::class.java)
    }

    val tmdbtvShowService: TmdbTvShowService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(OkHttpClient.Builder().build())
            .build()
            .create(TmdbTvShowService::class.java)
    }

    val tmdbActorService: TmdbActorService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(OkHttpClient.Builder().build())
            .build()
            .create(TmdbActorService::class.java)
    }

    val tmdbSearchService: TmdbSearchService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(OkHttpClient.Builder().build())
            .build()
            .create(TmdbSearchService::class.java)
    }

    val tmdbAccountService: TmdbAccountService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(OkHttpClient.Builder().build())
            .build()
            .create(TmdbAccountService::class.java)
    }
}