package com.marius.personalimdb.ui.movies.category.popular

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.data.repository.MovieRepository

class MoviesPopularViewModel : ViewModel() {
    val movieList = MutableLiveData(emptyList<Movie>())
    var isLoading = false
    var lastLoadedPage = 0
    var totalPages = 5

    fun initMovies() {
        getMovies(1)
    }

    fun getMovies(page: Int) {
        if (page > totalPages) {
            return
        }
        if (page == 1 && lastLoadedPage > 0)
            return
        if (!isLoading) {
            isLoading = true
            MovieRepository.getPopularMovies(page) { details ->
                movieList.value = details.movieList
                totalPages = details.totalPages
                lastLoadedPage++
                isLoading = false
            }
        }
    }
}