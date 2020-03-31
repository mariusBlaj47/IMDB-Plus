package com.marius.personalimdb.ui.movies.category.trending

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marius.personalimdb.R
import com.marius.personalimdb.adapter.MovieAdapter
import com.marius.personalimdb.helper.interfaces.OpensMovieDetails
import com.marius.personalimdb.ui.movies.details.MovieDetailsActivity
import kotlinx.android.synthetic.main.fragment_movies_trending.*


class MoviesTrendingFragment : Fragment(),
    OpensMovieDetails {

    override fun onMovieClicked(movieId: Int) {
        val intent = Intent(context, MovieDetailsActivity::class.java).apply {
            putExtra("movieId", movieId)
        }
        startActivity(intent)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MoviesTrendingViewModel::class.java)
    }

    private val movieAdapter = MovieAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies_trending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpObservers()
        viewModel.initMovies()
    }

    private fun setUpObservers() {
        viewModel.movieList.observe(this, Observer {
            movieAdapter.addItems(
                it.toMutableList(),
                viewModel.totalPages != viewModel.lastLoadedPage
            )
        })
    }

    private fun setUpRecyclerView() {
        moviesTrendingRecycler.layoutManager = LinearLayoutManager(context)
        moviesTrendingRecycler.adapter = movieAdapter
        moviesTrendingRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.getMovies(viewModel.lastLoadedPage + 1)
                }
            }
        })
    }
}