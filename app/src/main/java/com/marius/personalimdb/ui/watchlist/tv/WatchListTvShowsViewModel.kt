package com.marius.personalimdb.ui.watchlist.tv

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.personalimdb.data.Account
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.data.repository.AccountRepository

class WatchListTvShowsViewModel : ViewModel() {
    val tvShowList = MutableLiveData(emptyList<TvShow>())
    var lastLoadedPage = 0
    var isLoading = false
    var totalPages = 1

    fun initTvShows() {
        lastLoadedPage = 0
        totalPages = 1
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
            Account.details.sessionId?.let { session ->
                AccountRepository.getWatchlistTvShows(session, page) { details ->
                    tvShowList.value = details.tvShowList
                    totalPages = details.totalPages
                    lastLoadedPage++
                    isLoading = false
                }
            }
        }
    }
}