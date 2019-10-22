package com.marius.personalimdb.ui.movies.category.top

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.moviedatabase.retrofitApi.responses.SearchMovieResponse
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.helper.filterAll
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            TmdbServiceFactory.tmdbMovieService.getTopMovies(
                TmdbServiceFactory.API_KEY,
                page
            )
                .enqueue(object : Callback<SearchMovieResponse> {
                    override fun onFailure(call: Call<SearchMovieResponse>, t: Throwable) {
                        Log.d("MoviesFragment", t.localizedMessage)
                    }

                    override fun onResponse(
                        call: Call<SearchMovieResponse>,
                        response: Response<SearchMovieResponse>
                    ) {
                        lastLoadedPage++
                        response.body()?.totalPages?.let { pages ->
                            totalPages = pages
                        }
                        response.body()?.results?.let {
                            val movies = it.filterAll()
                            movieList.value = movies
                            isLoading = false
                        }
                    }
                })
        }
    }
}