package com.marius.personalimdb.ui.tvShows.details

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.marius.personalimdb.adapter.TvShowSmallAdapter
import com.marius.personalimdb.databinding.ActivityTvShowDetailsBinding
import com.marius.personalimdb.helper.OpensActorDetails
import com.marius.personalimdb.helper.OpensTvShowDetails
import com.marius.personalimdb.helper.OpensYoutube
import com.marius.personalimdb.ui.actors.details.ActorDetailsActivity
import kotlinx.android.synthetic.main.activity_tv_show_details.*

class TvShowDetailsActivity : AppCompatActivity(), OpensYoutube, OpensActorDetails,
    OpensTvShowDetails {
    override fun onTvShowClicked(tvShowId: Int) {
        val intent = Intent(this, TvShowDetailsActivity::class.java).apply {
            putExtra("tvShowId", tvShowId)
        }
        startActivity(intent)
    }

    override fun onActorClicked(actorId: Int) {
        val intent = Intent(this, ActorDetailsActivity::class.java).apply {
            putExtra("actorId", actorId)
        }
        startActivity(intent)
    }

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

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(TvShowDetailsViewModel::class.java)
    }

    private val pagerAdapter = ImagePagerAdapter(supportFragmentManager, this)
    private val castAdapter = ActorSmallAdapter(this)
    private val similarAdapter = TvShowSmallAdapter(this)
    private val recommendedAdapter = TvShowSmallAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityTvShowDetailsBinding =
            DataBindingUtil.setContentView(
                this,
                R.layout.activity_tv_show_details
            )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        intent.extras?.getInt("tvShowId")?.let {
            viewModel.getData(it)
        }
        imagesViewPager.adapter = pagerAdapter
        setUpObservers()
        setUpRecyclerViews()
    }

    private fun setUpRecyclerViews() {
        castRecycler2.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        castRecycler2.adapter = castAdapter
        similarTvShowsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        similarTvShowsRecycler.adapter = similarAdapter
        recommendedTvShowsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recommendedTvShowsRecycler.adapter = recommendedAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun setUpObservers() {
        viewModel.tvShow.observe(this, Observer {
            Glide.with(this)
                .load("http://image.tmdb.org/t/p/w154" + it.poster)
                .centerCrop()
                .into(poster)
        })
        viewModel.releaseYear.observe(this, Observer {
            releaseDate.text = "$it ● "
        })
        viewModel.trailer.observe(this, Observer {
            pagerAdapter.addPhotos(mutableListOf(it))
        })
        viewModel.posters.observe(this, Observer {
            pagerAdapter.addPhotos(it)
            circleIndicator2.setViewPager(imagesViewPager)
        })
        viewModel.actorList.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                castAdapter.addActors(it)
                noCastTv2.visibility = View.GONE
            } else noCastTv2.visibility = View.VISIBLE
        })
        viewModel.similarTvShows.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                similarAdapter.addTvShows(it)
                noSimilarTv2.visibility = View.GONE
            } else noSimilarTv2.visibility = View.VISIBLE
        })
        viewModel.recommendedTvShows.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                recommendedAdapter.addTvShows(it)
                noRecommendationTv2.visibility = View.GONE
            } else noRecommendationTv2.visibility = View.VISIBLE
        })
        viewModel.startImdb.observe(this, Observer {
            viewModel.tvShow.value?.externals?.imdbId?.let { tvShow ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://www.imdb.com/title/$tvShow")
                    )
                )
            }
        })
    }
}
