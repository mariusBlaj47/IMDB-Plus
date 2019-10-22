package com.marius.personalimdb.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.marius.personalimdb.R
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.databinding.ItemMovieSmallBinding
import com.marius.personalimdb.helper.OpensMovieDetails
import com.marius.personalimdb.ui.movies.ItemMovieViewModel

class MovieSmallAdapter(
    private val callback: OpensMovieDetails
) :
    RecyclerView.Adapter<MovieSmallAdapter.MovieVH>(), OpensMovieDetails {


    private val movieList = mutableListOf<Movie>()

    fun addMovies(movies: List<Movie>) {
        movieList.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onMovieClicked(movieId: Int) {
        callback.onMovieClicked(movieId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        return MovieVH(parent)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        holder.bind(movieList[position])
    }

    inner class MovieVH(
        private val parent: ViewGroup,
        private val binding: ItemMovieSmallBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_movie_small,
            parent,
            false
        )
    ) : MovieAdapter.ViewHolder(binding.root) {
        @SuppressLint("NewApi")
        fun bind(movie: Movie) {
            binding.viewModel =
                ItemMovieViewModel(movie, this@MovieSmallAdapter)
        }
    }

}