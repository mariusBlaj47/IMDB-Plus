package com.marius.personalimdb.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.marius.personalimdb.R
import com.marius.personalimdb.data.model.Actor
import com.marius.personalimdb.databinding.ItemActorSmallBinding
import com.marius.personalimdb.helper.OpensActorDetails
import com.marius.personalimdb.ui.actors.ItemActorViewModel

class ActorSmallAdapter(private val callback: OpensActorDetails) :
    RecyclerView.Adapter<ActorSmallAdapter.ActorVH>(), OpensActorDetails {
    override fun onActorClicked(actorId: Int) {
        callback.onActorClicked(actorId)
    }

    private val actorList = mutableListOf<Actor>()

    fun addActors(actors: MutableList<Actor>) {
        actorList.clear()
        actorList.addAll(actors)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorVH {
        return ActorVH(parent)
    }

    override fun getItemCount(): Int {
        return actorList.size
    }

    override fun onBindViewHolder(holder: ActorVH, position: Int) {
        holder.bind(actorList[position])
    }

    inner class ActorVH(
        private val parent: ViewGroup,
        private val binding: ItemActorSmallBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_actor_small,
            parent,
            false
        )
    ) : MovieAdapter.ViewHolder(binding.root) {
        @SuppressLint("NewApi")
        fun bind(actor: Actor) {
            binding.viewModel = ItemActorViewModel(actor, this@ActorSmallAdapter)
        }
    }

}