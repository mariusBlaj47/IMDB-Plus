package com.marius.personalimdb.ui.movies.category.trending

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.data.repository.MovieRepository

class MoviesTrendingViewModel : ViewModel() {
    var movieList = MutableLiveData(emptyList<Movie>())
    var lastLoadedPage = -1
    var isLoading = false
    var totalPages = 1

    fun initMovies() {
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
            MovieRepository.getTrendingMovies(page) { details ->
                movieList.value = details.movieList
                totalPages = details.totalPages
                lastLoadedPage++
                isLoading = false
            }
        }
    }
}