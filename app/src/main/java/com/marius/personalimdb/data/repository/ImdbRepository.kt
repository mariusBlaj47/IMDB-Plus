package com.marius.personalimdb.data.repository

import android.util.Log
import com.marius.personalimdb.data.imdbServices.ImdbMovieServiceFactory
import com.marius.personalimdb.data.model.ImdbRating
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ImdbRepository {

    /**
     * Gets the rating of the movie/TV Show on IMDB
     */
    fun getRating(imdbId: String, callback: (ImdbRating) -> Unit) {
        ImdbMovieServiceFactory.tmdbMovieService.getRating(imdbId)
            .enqueue(object : Callback<ImdbRating> {
                override fun onFailure(call: Call<ImdbRating>, t: Throwable) {
                    Log.d("MovieListDetails", t.localizedMessage)
                }

                override fun onResponse(call: Call<ImdbRating>, response: Response<ImdbRating>) {
                    response.body()?.let {
                        callback(it)
                    }
                }
            })
    }
}