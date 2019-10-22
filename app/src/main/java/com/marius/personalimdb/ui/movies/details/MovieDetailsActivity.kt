package com.marius.personalimdb.ui.movies.details

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.marius.personalimdb.R
import com.marius.personalimdb.adapter.ActorSmallAdapter
import com.marius.personalimdb.adapter.ImagePagerAdapter
import com.marius.personalimdb.adapter.MovieSmallAdapter
import com.marius.personalimdb.databinding.ActivityMovieDetailsBinding
import com.marius.personalimdb.helper.OpensActorDetails
import com.marius.personalimdb.helper.OpensMovieDetails
import com.marius.personalimdb.helper.OpensYoutube
import com.marius.personalimdb.ui.actors.details.ActorDetailsActivity
import kotlinx.android.synthetic.main.activity_movie_details.*

class MovieDetailsActivity : AppCompatActivity(), OpensMovieDetails, OpensActorDetails,
    OpensYoutube {

    override fun onVideoClicked() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + viewModel.trailer.value?.key)
            ).apply {
                putExtra("force_fullscreen", true)
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
        )
    }

    override fun onActorClicked(actorId: Int) {
        val intent = Intent(this, ActorDetailsActivity::class.java).apply {
            putExtra("actorId", actorId)
        }
        startActivity(intent)
    }

    override fun onMovieClicked(movieId: Int) {
        val intent = Intent(this, MovieDetailsActivity::class.java).apply {
            putExtra("movieId", movieId)
        }
        startActivity(intent)
    }

    private val pagerAdapter = ImagePagerAdapter(supportFragmentManager, this)
    private val similarAdapter = MovieSmallAdapter(this)
    private val recommendedAdapter = MovieSmallAdapter(this)
    private val castAdapter = ActorSmallAdapter(this)

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MovieDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMovieDetailsBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_movie_details
            )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        intent.extras?.getInt("movieId")?.let {
            viewModel.getData(it)
            Log.d("movieId", it.toString())
        }

        imagesViewPager.adapter = pagerAdapter
        setUpObservers()
        setUpRecyclerViews()
    }

    private fun setUpRecyclerViews() {
        similarMovieRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        similarMovieRecycler.adapter = similarAdapter
        recommendedRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recommendedRecycler.adapter = recommendedAdapter
        castRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        castRecycler.adapter = castAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun setUpObservers() {
        viewModel.movie.observe(this, Observer {
            Glide.with(this)
                .load("http://image.tmdb.org/t/p/w154" + it.poster)
                .centerCrop()
                .into(poster)
        })
        viewModel.releaseDate.observe(this, Observer {
            releaseDate.text = "$it ● "
        })
        viewModel.similarMovies.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                similarAdapter.addMovies(it)
                noSimilarTv.visibility = View.GONE
            } else noSimilarTv.visibility = View.VISIBLE
        })
        viewModel.recommendedMovies.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                recommendedAdapter.addMovies(it)
                noRecommendationTv.visibility = View.GONE
            } else noRecommendationTv.visibility = View.VISIBLE
        })
        viewModel.actorList.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                castAdapter.addActors(it)
                noCastTv.visibility = View.GONE
            } else noCastTv.visibility = View.VISIBLE
        })
        viewModel.trailer.observe(this, Observer {
            pagerAdapter.addPhotos(mutableListOf(it))
        })
        viewModel.posters.observe(this, Observer {
            pagerAdapter.addPhotos(it)
            circleIndicator.setViewPager(imagesViewPager)
        })
        viewModel.startImdb.observe(this, Observer {
            viewModel.movie.value?.imdbId?.let {movie->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.imdb.com/title/$movie")
                    )
                )
            }
        })
    }
}
