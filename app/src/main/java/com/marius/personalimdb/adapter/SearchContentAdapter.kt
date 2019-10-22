package com.marius.personalimdb.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.marius.personalimdb.R
import com.marius.personalimdb.data.model.Actor
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.data.model.SearchContent
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.databinding.ItemActorBinding
import com.marius.personalimdb.databinding.ItemMovieBinding
import com.marius.personalimdb.databinding.ItemTvShowBinding
import com.marius.personalimdb.helper.OpensDetails
import com.marius.personalimdb.ui.actors.ItemActorViewModel
import com.marius.personalimdb.ui.movies.ItemMovieViewModel
import com.marius.personalimdb.ui.tvShows.ItemTvShowViewModel

class SearchContentAdapter(private val callback: OpensDetails) :
    RecyclerView.Adapter<SearchContentAdapter.ViewHolder>(), OpensDetails {
    override fun onActorClicked(actorId: Int) {
        callback.onActorClicked(actorId)
    }

    override fun onMovieClicked(movieId: Int) {
        callback.onMovieClicked(movieId)
    }

    override fun onTvShowClicked(tvShowId: Int) {
        callback.onTvShowClicked(tvShowId)
    }

    private val movie = 1
    private val tv = 2
    private val actor = 3
    private val data = mutableListOf<SearchContent>()

    fun clearItems() {
        data.clear()
    }

    fun addItem(newItem: SearchContent) {
        data.add(newItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            movie -> MovieVH(parent)
            actor -> ActorVH(parent)
            else -> TvShowVH(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (data[position]) {
            is Actor -> actor
            is TvShow -> tv
            is Movie -> movie
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is MovieVH -> holder.bind(data[position] as Movie)
            is ActorVH -> holder.bind(data[position] as Actor)
            is TvShowVH -> holder.bind(data[position] as TvShow)
        }
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
    ) : ViewHolder(binding.root) {
        @SuppressLint("NewApi")
        fun bind(actor: Actor) {
            binding.viewModel = ItemActorViewModel(actor, this@SearchContentAdapter)
        }
    }

    inner class MovieVH(
        private val parent: ViewGroup,
        private val binding: ItemMovieBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_movie,
            parent,
            false
        )
    ) : ViewHolder(binding.root) {

        @SuppressLint("NewApi")
        fun bind(movie: Movie) {
            binding.viewModel =
                ItemMovieViewModel(movie, this@SearchContentAdapter)
        }
    }

    inner class TvShowVH(
        private val parent: ViewGroup,
        private val binding: ItemTvShowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_tv_show,
            parent,
            false
        )
    ) : ViewHolder(binding.root) {

        @SuppressLint("NewApi")
        fun bind(tvShow: TvShow) {
            binding.viewModel =
                ItemTvShowViewModel(tvShow, this@SearchContentAdapter)
        }
    }
}