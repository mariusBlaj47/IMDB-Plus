package com.marius.personalimdb.ui.actors.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.personalimdb.data.model.Actor
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.data.model.Photo
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.data.repository.ActorRepository

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
        ActorRepository.getPhotos(actorId) {
            posters.value = it.take(6)
        }
    }

    private fun getMovies(actorId: Int) {
        ActorRepository.getActorMovies(actorId) {
            movies.value = it
        }
    }

    private fun getTvShows(actorId: Int) {
        ActorRepository.getActorTvShows(actorId) {
            tvShows.value = it
        }
    }

    private fun getInfo(actorId: Int) {
        ActorRepository.getDetails(actorId) {
            actor.value = it
        }
    }
}