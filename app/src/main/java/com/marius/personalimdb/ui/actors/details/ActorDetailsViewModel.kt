package com.marius.personalimdb.ui.actors.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory.API_KEY
import com.marius.personalimdb.data.model.Actor
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.data.model.Photo
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.data.response.ImagesResponse
import com.marius.personalimdb.data.response.MovieCreditsResponse
import com.marius.personalimdb.data.response.TvShowCreditsResponse
import com.marius.personalimdb.helper.filterAll
import com.marius.personalimdb.helper.filterAllTvShows
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActorDetailsViewModel : ViewModel() {
    val actor = MutableLiveData<Actor>()
    val movies = MutableLiveData<List<Movie>>()
    val tvShows = MutableLiveData<List<TvShow>>()
    val posters = MutableLiveData<List<Photo>>()
    val dimension = 200

    fun getData(actorId: Int) {
        getInfo(actorId)
        getMovies(actorId)
        getTvShows(actorId)
        getPosters(actorId)
    }

    private fun getPosters(actorId: Int) {
        TmdbServiceFactory.tmdbActorService.getActorPosters(actorId, API_KEY)
            .enqueue(object : Callback<ImagesResponse> {
                override fun onFailure(call: Call<ImagesResponse>, t: Throwable) {
                    Log.d("ActorDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<ImagesResponse>,
                    response: Response<ImagesResponse>
                ) {
                    response.body()?.posters?.let {
                        val filteredPosters = it
                        filteredPosters.filter {
                            it.ratio > 1
                        }
                        posters.value = filteredPosters.take(6)
                    }
                }
            })
    }

    private fun getMovies(actorId: Int) {
        TmdbServiceFactory.tmdbActorService.getActorMovies(actorId, API_KEY)
            .enqueue(object : Callback<MovieCreditsResponse> {
                override fun onFailure(call: Call<MovieCreditsResponse>, t: Throwable) {
                    Log.d("ActorDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<MovieCreditsResponse>,
                    response: Response<MovieCreditsResponse>
                ) {
                    response.body()?.movies?.let {
                        val moviesFiltered = it.filterAll()
                        movies.value = moviesFiltered.sortedByDescending {
                            it.popularity
                        }
                    }
                }

            })
    }

    private fun getTvShows(actorId: Int) {
        TmdbServiceFactory.tmdbActorService.getActorTvShows(actorId, API_KEY)
            .enqueue(object : Callback<TvShowCreditsResponse> {
                override fun onFailure(call: Call<TvShowCreditsResponse>, t: Throwable) {
                    Log.d("ActorDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<TvShowCreditsResponse>,
                    response: Response<TvShowCreditsResponse>
                ) {
                    response.body()?.movies?.let {
                        val tvShowsFiltered = it.filterAllTvShows()
                        tvShows.value = tvShowsFiltered.sortedByDescending {
                            it.popularity
                        }
                    }
                }

            })
    }

    private fun getInfo(actorId: Int) {
        TmdbServiceFactory.tmdbActorService.getActorDetails(actorId, API_KEY)
            .enqueue(object : Callback<Actor> {
                override fun onFailure(call: Call<Actor>, t: Throwable) {
                    Log.d("ActorDetails", t.localizedMessage)
                }

                override fun onResponse(call: Call<Actor>, response: Response<Actor>) {
                    response.body()?.let {
                        actor.value = it
                    }
                }

            })
    }
}