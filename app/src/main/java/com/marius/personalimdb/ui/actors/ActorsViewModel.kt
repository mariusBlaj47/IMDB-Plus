package com.marius.personalimdb.ui.actors

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory
import com.marius.moviedatabase.retrofitApi.TmdbServiceFactory.API_KEY
import com.marius.personalimdb.data.model.Actor
import com.marius.personalimdb.data.response.SearchPeopleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActorsViewModel : ViewModel() {
    val actorList = MutableLiveData(listOf<Actor>())

    fun initActors() {
        getActors(1)
    }

    //TODO : Maybe finish page support

    fun getActors(page: Int = 1) {
        TmdbServiceFactory.tmdbActorService.getPopularPeople(API_KEY, page).enqueue(object :
            Callback<SearchPeopleResponse> {
            override fun onFailure(call: Call<SearchPeopleResponse>, t: Throwable) {
                Log.d("actors", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<SearchPeopleResponse>,
                response: Response<SearchPeopleResponse>
            ) {
                response.body()?.results?.let {
                    val actors = it.toMutableList().filter {
                        !it.credits.isNullOrEmpty()
                    }
                    actors.forEach {
                        it.credits?.sortByDescending {
                            it.votes
                        }
                        it.credits = it.credits?.take(3)?.toMutableList()
                        Log.d("actors", it.credits.toString())
                    }
                    actorList.value = actors

                }
            }

        })
    }
}