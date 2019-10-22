package com.marius.personalimdb.ui.tvShows.category.top

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory.API_KEY
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.data.response.SearchTvShowResponse
import com.marius.personalimdb.helper.filterAllTvShows
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowsTopViewModel : ViewModel() {
    val tvShowList = MutableLiveData(emptyList<TvShow>())
    var lastLoadedPage = 0
    var isLoading = false
    var totalPages = 1

    fun initTvShows() {
        getTvShows(1)
    }

    fun getTvShows(page: Int) {
        if (page > totalPages) {
            return
        }
        if (page - 1 < lastLoadedPage)
            return
        if (!isLoading) {
            isLoading = true
            TmdbServiceFactory.tmdbtvShowService.getTopRatedTvShows(API_KEY, page)
                .enqueue(object : Callback<SearchTvShowResponse> {
                    override fun onFailure(call: Call<SearchTvShowResponse>, t: Throwable) {
                        Log.d("TvShow", t.localizedMessage)
                    }

                    override fun onResponse(
                        call: Call<SearchTvShowResponse>,
                        response: Response<SearchTvShowResponse>
                    ) {
                        lastLoadedPage++
                        response.body()?.totalPages?.let { pages ->
                            totalPages = pages
                        }
                        response.body()?.results?.let {
                            val filteredShows = it.filterAllTvShows().toMutableList()
                            //The #1 show is weird AF so i removed it from the list
                            filteredShows.remove(filteredShows.first())
                            tvShowList.value = filteredShows
                        }
                        lastLoadedPage++
                    }

                })
        }
    }
}