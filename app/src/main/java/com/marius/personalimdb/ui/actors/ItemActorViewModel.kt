package com.marius.personalimdb.ui.actors

import androidx.lifecycle.MutableLiveData
import com.marius.personalimdb.data.model.Actor
import com.marius.personalimdb.helper.OpensActorDetails

class ItemActorViewModel(actor: Actor, private val callback: OpensActorDetails) {
    val actor = MutableLiveData<Actor>()
    val gender = MutableLiveData<String>()

    init {
        this.actor.value = actor
        this.actor.value?.role?.let {
            this.actor.value?.role = actor.role?.split("/")?.get(0)
        }
        //Pull to refresh
        if (actor.gender == 1)
            gender.value = "Actress"
        else gender.value = "Actor"
    }

    val dimension = 200

    fun onClick() {
        actor.value?.id?.let { callback.onActorClicked(it) }
    }
}