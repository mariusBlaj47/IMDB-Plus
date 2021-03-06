package com.marius.personalimdb.ui.tvShows.category.trending

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.data.repository.TvShowRepository
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
            TvShowRepository.getTrendingTvShows(page){ details ->
                tvShowList.value = details.tvShowList
                totalPages = details.totalPages
                lastLoadedPage++
                isLoading = false
            }
        }
    }
}