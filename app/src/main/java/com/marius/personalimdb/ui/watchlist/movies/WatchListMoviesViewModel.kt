package com.marius.personalimdb.ui.watchlist.movies

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.personalimdb.data.Account
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.data.repository.AccountRepository

class WatchListMoviesViewModel : ViewModel() {
    val movieList = MutableLiveData(emptyList<Movie>())
    var lastLoadedPage = 0
    var isLoading = false
    var totalPages = 1

    fun initMovies() {
        lastLoadedPage = 0
        totalPages = 1
        getMovies(1)
    }

    fun getMovies(page: Int) {
        if (page > totalPages) {
            return
        }
        if (page - 1 < lastLoadedPage)
            return
        if (!isLoading) {
            isLoading = true
            Account.details.sessionId?.let { session ->
                AccountRepository.getWatchlistMovies(session, page) { details ->
                    movieList.value = details.movieList
                    details.movieList.forEach {
                        Log.d("DEBUG", it.toString())
                    }
                    totalPages = details.totalPages
                    lastLoadedPage++
                    isLoading = false
                }
            }
        }
    }
}