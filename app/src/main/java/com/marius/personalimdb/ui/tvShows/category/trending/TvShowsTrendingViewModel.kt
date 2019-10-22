package com.marius.personalimdb.ui.tvShows.category.trending

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.data.response.SearchTvShowResponse
import com.marius.personalimdb.helper.filterAllTvShows
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowsTrendingViewModel : ViewModel() {
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
            TmdbServiceFactory.tmdbtvShowService.getTrendingTvShows(
                TmdbServiceFactory.API_KEY,
                page
            )
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
                            tvShowList.value = it.filterAllTvShows()
                        }
                        isLoading = false
                    }

                })
        }
    }
}