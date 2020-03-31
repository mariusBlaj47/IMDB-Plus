package com.marius.personalimdb.ui.tvShows.category.netflix

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.data.repository.TvShowRepository

class TvShowsNetflixViewModel : ViewModel() {
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
            TvShowRepository.getNetflixTvShows(page) { details ->
                tvShowList.value = details.tvShowList
                totalPages = details.totalPages
                lastLoadedPage++
                isLoading = false
            }
        }
    }
}