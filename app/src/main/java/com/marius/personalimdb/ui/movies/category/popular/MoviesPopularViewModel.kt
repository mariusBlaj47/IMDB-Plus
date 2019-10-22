package com.marius.personalimdb.ui.movies.category.popular

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory.API_KEY
import com.marius.moviedatabase.retrofitApi.responses.SearchMovieResponse
import com.marius.personalimdb.data.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

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
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date1 = Date(System.currentTimeMillis())
            val date2 = Calendar.getInstance()
            date2.add(Calendar.DAY_OF_YEAR, -300)
            val firstDateString = formatter.format(date1)
            val secondDateString = formatter.format(date2.timeInMillis)
            TmdbServiceFactory.tmdbMovieService.getPopularMovies(
                API_KEY,
                secondDateString,
                firstDateString,
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
                            movieList.value = it
                        }
                        isLoading = false
                    }
                })
        }
    }
}