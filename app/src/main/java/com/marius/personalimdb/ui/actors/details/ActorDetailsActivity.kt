package com.marius.personalimdb.ui.actors.details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.marius.personalimdb.R
import com.marius.personalimdb.adapter.ImagePagerAdapter
import com.marius.personalimdb.adapter.MovieSmallAdapter
import com.marius.personalimdb.adapter.TvShowSmallAdapter
import com.marius.personalimdb.databinding.ActivityActorDetailsBinding
import com.marius.personalimdb.helper.OpensMovieDetails
import com.marius.personalimdb.helper.OpensTvShowDetails
import com.marius.personalimdb.ui.movies.details.MovieDetailsActivity
import com.marius.personalimdb.ui.tvShows.details.TvShowDetailsActivity
import kotlinx.android.synthetic.main.activity_actor_details.*

class ActorDetailsActivity : AppCompatActivity(), OpensMovieDetails, OpensTvShowDetails {
    override fun onTvShowClicked(tvShowId: Int) {
        val intent = Intent(this, TvShowDetailsActivity::class.java).apply {
            putExtra("tvShowId", tvShowId)
        }
        startActivity(intent)
    }

    override fun onMovieClicked(movieId: Int) {
        val intent = Intent(this, MovieDetailsActivity::class.java).apply {
            putExtra("movieId", movieId)
        }
        startActivity(intent)
    }

    private val movieAdapter = MovieSmallAdapter(this)
    private val tvShowsAdapter = TvShowSmallAdapter(this)
    private val pagerAdapter = ImagePagerAdapter(supportFragmentManager)
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ActorDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityActorDetailsBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_actor_details
            )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        intent.extras?.getInt("actorId")?.let { viewModel.getData(it) }

        imagesPager.adapter = pagerAdapter

        setUpObservers()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        movieRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        movieRecycler.adapter = movieAdapter
        tvShowsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        tvShowsRecycler.adapter = tvShowsAdapter
    }

    private fun setUpObservers() {
        viewModel.movies.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                noMoviesTv.visibility = View.VISIBLE
            } else {
                noMoviesTv.visibility = View.GONE
                movieAdapter.addMovies(it)
            }
        })
        viewModel.tvShows.observe(this, Observer {
            if (it.isNullOrEmpty()) {
                noTvShowsTv.visibility = View.VISIBLE
            } else {
                noTvShowsTv.visibility = View.GONE
                tvShowsAdapter.addTvShows(it.toMutableList())
            }
        })
        viewModel.posters.observe(this, Observer {
            Log.d("asde", it.toString())
            pagerAdapter.addPhotos(it.toMutableList())
            indicator.setViewPager(imagesPager)
        })
    }
}
