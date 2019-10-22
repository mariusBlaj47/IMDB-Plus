package com.marius.personalimdb.ui.watchlist.movies

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
import com.marius.personalimdb.helper.OpensMovieDetails
import com.marius.personalimdb.ui.movies.details.MovieDetailsActivity
import kotlinx.android.synthetic.main.fragment_watchlist_movies.*

class WatchListMoviesFragment : Fragment(), OpensMovieDetails {
    override fun onMovieClicked(movieId: Int) {
        val intent = Intent(context, MovieDetailsActivity::class.java).apply {
            putExtra("movieId", movieId)
        }
        startActivity(intent)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(WatchListMoviesViewModel::class.java)
    }

    private val movieAdapter = MovieAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_watchlist_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpObservers()
        setUpPullRefresh()
        viewModel.initMovies()
    }

    private fun setUpPullRefresh() {
        pullToRefresh.setOnRefreshListener {
            movieAdapter.clearItems()
            viewModel.lastLoadedPage = 0
            viewModel.initMovies()
            pullToRefresh.isRefreshing = false
        }
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
        moviesWatchlist.layoutManager = LinearLayoutManager(context)
        moviesWatchlist.adapter = movieAdapter
        moviesWatchlist.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.getMovies(viewModel.lastLoadedPage + 1)
                }
            }
        })
    }
}