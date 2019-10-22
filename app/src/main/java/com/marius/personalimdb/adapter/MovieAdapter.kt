package com.marius.personalimdb.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.marius.personalimdb.R
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.databinding.ItemMovieBinding
import com.marius.personalimdb.helper.OpensMovieDetails
import com.marius.personalimdb.ui.movies.ItemMovieViewModel

class MovieAdapter(private val callback: OpensMovieDetails) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>(), OpensMovieDetails {
    override fun onMovieClicked(movieId: Int) {
        callback.onMovieClicked(movieId)
    }

    private val loadingItem = Movie(-1, "1", "1", 1f, 1, "1", null, 1, emptyList(), 0f, null)
    private val loading = 1
    private val movie = 0
    private val data = mutableListOf<Movie>()

    init {
        data.add(loadingItem)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            movie -> {
                MovieVH(parent)
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
        else movie
    }

    fun clearItems() {
        data.clear()
        data.add(loadingItem)
        notifyDataSetChanged()
    }

    fun addItems(newData: MutableList<Movie>, addLoading: Boolean = true) {
        if (data.isNotEmpty())
            data.removeAt(data.lastIndex)
        data.addAll(newData)
        if (addLoading) {
            data.add(loadingItem)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is MovieVH)
            holder.bind(data[position])
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class LoadingVH(itemView: View) : ViewHolder(itemView)

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
                ItemMovieViewModel(movie, this@MovieAdapter)
        }
    }
}

