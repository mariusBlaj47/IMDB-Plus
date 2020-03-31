package com.marius.personalimdb.data.repository

import android.util.Log
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.personalimdb.data.model.SearchContent
import com.marius.personalimdb.data.repository.callbacks.SearchDetails
import com.marius.personalimdb.data.response.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SearchRepository {

    fun searchByQuery(query: String, page: Int, callback: (SearchDetails) -> Unit) {
        var totalPages = 1
        TmdbServiceFactory.tmdbSearchService.searchContent(query, TmdbServiceFactory.API_KEY, page)
            .enqueue(object :
                Callback<SearchResponse> {
                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    Log.d("searchfail", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    response.body()?.totalPages?.let {
                        totalPages = it
                    }

                    response.body()?.results?.let {
                        val searchList = mutableListOf<SearchContent>()
                        it.forEach {
                            when (it.type) {
                                "movie" -> {
                                    if (!it.description.isNullOrEmpty() && !it.poster.isNullOrEmpty() && !it.releaseDate.isNullOrEmpty()) {
                                        searchList.add(it.toMovie())
                                    }
                                }
                                "tv" -> {
                                    if (!it.description.isNullOrEmpty() && !it.poster.isNullOrEmpty() && !it.firstAirDate.isNullOrEmpty()) {
                                        searchList.add(it.toTvShow())
                                    }
                                }
                                else -> {
                                    if (!it.credits.isNullOrEmpty()) {
                                        searchList.add(it.toActor())
                                    }
                                }

                            }
                        }
                        callback(
                            SearchDetails(
                                searchList,
                                totalPages
                            )
                        )
                    }
                }
            })
    }
}