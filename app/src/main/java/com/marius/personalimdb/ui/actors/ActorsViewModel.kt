package com.marius.personalimdb.ui.actors

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marius.personalimdb.data.model.Actor
import com.marius.personalimdb.data.repository.ActorRepository

class ActorsViewModel : ViewModel() {
    val actorList = MutableLiveData(listOf<Actor>())
    var isLoading = false
    var lastLoadedPage = 0
    var totalPages = 5

    fun initActors() {
        getActors(1)
    }

    fun getActors(page: Int = 1) {
        if (page > totalPages) {
            return
        }
        if (page == 1 && lastLoadedPage > 0)
            return
        if (!isLoading) {
            isLoading = true
            ActorRepository.getPopularActors(page) { actors, pages ->
                actorList.value = actors
                totalPages = pages
                lastLoadedPage++
                isLoading = false
            }
        }
    }
}