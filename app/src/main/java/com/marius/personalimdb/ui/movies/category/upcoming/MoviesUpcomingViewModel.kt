package com.marius.personalimdb.ui.movies.category.upcoming

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
import java.text.SimpleDateFormat
import java.util.*


class MoviesUpcomingViewModel : ViewModel() {
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
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date1 = Date(System.currentTimeMillis())
            val date2 = Calendar.getInstance()
            date2.add(Calendar.DAY_OF_YEAR, 30)
            val firstDateString = formatter.format(date1)
            val secondDateString = formatter.format(date2.timeInMillis)
            TmdbServiceFactory.tmdbMovieService.getUpcomingMovies(
                TmdbServiceFactory.API_KEY,
                firstDateString,
                secondDateString, page
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
                            movieList.value = it.filterAll()
                            isLoading = false
                        }
                    }
                })
        }
    }
}