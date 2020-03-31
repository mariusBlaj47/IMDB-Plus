package com.marius.personalimdb.ui.movies.category.top

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.data.repository.MovieRepository

class MoviesTopViewModel : ViewModel() {
    val movieList = MutableLiveData(emptyList<Movie>())
    var lastLoadedPage = 0
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
            MovieRepository.getTopMovies(page) { details ->
                movieList.value = details.movieList
                totalPages = details.totalPages
                lastLoadedPage++
                isLoading = false
            }
        }
    }
}