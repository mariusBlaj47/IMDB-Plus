package com.marius.personalimdb.data.repository

import android.util.Log
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.personalimdb.data.model.Actor
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.data.model.Photo
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.data.response.ImagesResponse
import com.marius.personalimdb.data.response.MovieCreditsResponse
import com.marius.personalimdb.data.response.SearchPeopleResponse
import com.marius.personalimdb.data.response.TvShowCreditsResponse
import com.marius.personalimdb.helper.filterAll
import com.marius.personalimdb.helper.filterAllTvShows
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ActorRepository {

    /**
     * Gets the actors sorted by popularity
     */
    fun getPopularActors(page: Int, callback: (List<Actor>, Int) -> Unit) {
        TmdbServiceFactory.tmdbActorService.getPopularPeople(TmdbServiceFactory.API_KEY, page)
            .enqueue(object :
                Callback<SearchPeopleResponse> {
                override fun onFailure(call: Call<SearchPeopleResponse>, t: Throwable) {
                    Log.d("actors", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<SearchPeopleResponse>,
                    response: Response<SearchPeopleResponse>
                ) {
                    response.body()?.let {
                        val actors = mutableListOf<Actor>()
                        it.results?.let {
                            actors.addAll(it.toMutableList().filter {
                                !it.credits.isNullOrEmpty()
                            })
                            actors.forEach {
                                it.credits?.sortByDescending {
                                    it.votes
                                }
                                it.credits = it.credits?.take(3)?.toMutableList()
                            }
                        }
                        callback(actors, it.totalPages)
                    }
                }
            })
    }

    /**
     * Gets the details about an actor
     */
    fun getDetails(actorId: Int, callback: (Actor) -> Unit) {
        TmdbServiceFactory.tmdbActorService.getActorDetails(actorId, TmdbServiceFactory.API_KEY)
            .enqueue(object : Callback<Actor> {
                override fun onFailure(call: Call<Actor>, t: Throwable) {
                    Log.d("ActorDetails", t.localizedMessage)
                }

                override fun onResponse(call: Call<Actor>, response: Response<Actor>) {
                    response.body()?.let {
                        callback(it)
                    }
                }
            })
    }

    /**
     * Gets the Tv Shows in which an actor starred
     */
    fun getActorTvShows(actorId: Int, callback: (List<TvShow>) -> Unit) {
        TmdbServiceFactory.tmdbActorService.getActorTvShows(actorId, TmdbServiceFactory.API_KEY)
            .enqueue(object : Callback<TvShowCreditsResponse> {
                override fun onFailure(call: Call<TvShowCreditsResponse>, t: Throwable) {
                    Log.d("ActorDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<TvShowCreditsResponse>,
                    response: Response<TvShowCreditsResponse>
                ) {
                    response.body()?.movies?.let {
                        callback(it.filterAllTvShows())
                    }
                }
            })
    }

    /**
     * Gets the movies in which an actor starred
     */
    fun getActorMovies(actorId: Int, callback: (List<Movie>) -> Unit) {
        TmdbServiceFactory.tmdbActorService.getActorMovies(actorId, TmdbServiceFactory.API_KEY)
            .enqueue(object : Callback<MovieCreditsResponse> {
                override fun onFailure(call: Call<MovieCreditsResponse>, t: Throwable) {
                    Log.d("ActorDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<MovieCreditsResponse>,
                    response: Response<MovieCreditsResponse>
                ) {
                    response.body()?.movies?.let {
                        callback(it.filterAll())
                    }
                }

            })
    }

    fun getPhotos(actorId: Int, callback: (List<Photo>) -> Unit) {
        TmdbServiceFactory.tmdbActorService.getActorPosters(actorId, TmdbServiceFactory.API_KEY)
            .enqueue(object : Callback<ImagesResponse> {
                override fun onFailure(call: Call<ImagesResponse>, t: Throwable) {
                    Log.d("ActorDetails", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<ImagesResponse>,
                    response: Response<ImagesResponse>
                ) {
                    response.body()?.profiles?.let {
                        val filteredPosters = it
                        filteredPosters.filter { photo ->
                            photo.ratio > 1
                        }
                        callback(filteredPosters)
                    }
                }
            })
    }
}