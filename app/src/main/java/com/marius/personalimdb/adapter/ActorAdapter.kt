package com.marius.personalimdb.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.marius.personalimdb.R
import com.marius.personalimdb.data.model.Actor
import com.marius.personalimdb.databinding.ItemActorBinding
import com.marius.personalimdb.helper.interfaces.OpensActorDetails
import com.marius.personalimdb.ui.actors.ItemActorViewModel

class ActorAdapter(private val callback: OpensActorDetails) :
    RecyclerView.Adapter<ActorAdapter.ViewHolder>(),
    OpensActorDetails {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            actor -> {
                ActorVH(parent)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_progress50dp, parent, false)
                LoadingVH(itemView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position] == loadingItem)
            loading
        else actor
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is ActorVH)
            holder.bind(data[position])
    }

    override fun onActorClicked(actorId: Int) {
        callback.onActorClicked(actorId)
    }

    private val loadingItem = Actor(-1, "", "", "", "", 1, null)
    private val loading = 1
    private val actor = 0
    private val data = mutableListOf<Actor>()

    init {
        data.add(loadingItem)
        notifyDataSetChanged()
    }

    fun addItems(newData: MutableList<Actor>, addLoading: Boolean = true) {
        if (data.isNotEmpty())
            data.removeAt(data.lastIndex)
        data.addAll(newData)
        if (addLoading) {
            data.add(loadingItem)
        }
        notifyDataSetChanged()
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class LoadingVH(itemView: View) : ViewHolder(itemView)

    inner class ActorVH(
        private val parent: ViewGroup,
        private val binding: ItemActorBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_actor,
            parent,
            false
        )
    ) : ActorAdapter.ViewHolder(binding.root) {
        @SuppressLint("NewApi")
        fun bind(actor: Actor) {
            binding.viewModel = ItemActorViewModel(actor, this@ActorAdapter)
        }
    }

}