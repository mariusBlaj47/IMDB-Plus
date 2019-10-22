package com.marius.personalimdb.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory.API_KEY
import com.marius.personalimdb.data.model.*
import com.marius.personalimdb.data.response.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    val queryString = MutableLiveData<String>()
    val resetTrigger = MutableLiveData<Unit>()
    val searchedItems = MutableLiveData(mutableListOf<SearchContent>())
    var isLoading = false
    var lastLoadedPage = 0
    var totalPages = 1

    fun initSearch() {
        searchContent()
    }

    fun extendSearchPage(page: Int) {
        if (page > totalPages) {
            return
        }
        isLoading = true
        searchContent(page)
    }

    private fun searchContent(page: Int = 1) {
        if (page == 1)
            lastLoadedPage = 0
        val query = queryString.value
        if (!query.isNullOrBlank()) {
            isLoading = true
            TmdbServiceFactory.tmdbSearchService.searchContent(query, API_KEY, page)
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
                        lastLoadedPage++

                        response.body()?.results?.let {
                            val searchList = mutableListOf<SearchContent>()
                            it.forEach {
                                when (it.type) {
                                    "movie" -> {
                                        if (!it.description.isNullOrEmpty() && !it.poster.isNullOrEmpty() && !it.releaseDate.isNullOrEmpty()) {
                                            val movie = getMovie(it)
                                            searchList.add(movie)
                                        }
                                    }
                                    "tv" -> {
                                        if (!it.description.isNullOrEmpty() && !it.poster.isNullOrEmpty() && !it.firstAirDate.isNullOrEmpty()) {
                                            val tvShow = getTvShow(it)
                                            searchList.add(tvShow)
                                        }
                                    }
                                    else -> {
                                        if (!it.credits.isNullOrEmpty()) {
                                            val actor = getActor(it)
                                            searchList.add(actor)
                                        }
                                    }

                                }

                            }
                            searchedItems.value = searchList
                        }
                        isLoading = false
                    }

                })
        }
    }

    private fun getMovie(it: SearchItem): Movie {
        return Movie(
            it.id,
            it.title,
            it.releaseDate,
            it.rating,
            1,
            it.description,
            it.poster,
            it.duration,
            it.genres,
            it.popularity,
            null
        )
    }

    private fun getTvShow(it: SearchItem): TvShow {
        return TvShow(
            it.id,
            it.name,
            it.firstAirDate,
            it.rating,
            1,
            it.description,
            it.poster,
            it.seasons,
            it.episodes,
            it.runtime,
            it.genres,
            it.popularity,
            it.lastAirDate,
            null
        )
    }

    private fun getActor(it: SearchItem): Actor {
        return Actor(
            it.id,
            it.name,
            it.role,
            it.profilePhoto,
            it.biography,
            it.gender,
            it.credits
        )
    }

}