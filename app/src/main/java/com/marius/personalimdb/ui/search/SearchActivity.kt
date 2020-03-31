package com.marius.personalimdb.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.marius.personalimdb.R
import com.marius.personalimdb.adapter.SearchContentAdapter
import com.marius.personalimdb.database.HistoryDatabase
import com.marius.personalimdb.database.WatchlistDatabase
import com.marius.personalimdb.databinding.ActivitySearchBinding
import com.marius.personalimdb.helper.interfaces.OpensDetails
import com.marius.personalimdb.ui.actors.details.ActorDetailsActivity
import com.marius.personalimdb.ui.movies.details.MovieDetailsActivity
import com.marius.personalimdb.ui.tvShows.details.TvShowDetailsActivity
import kotlinx.android.synthetic.main.activity_search.*
import kotlin.concurrent.thread


class SearchActivity : AppCompatActivity(), OpensDetails {
    override fun onActorClicked(actorId: Int) {
        val intent = Intent(this, ActorDetailsActivity::class.java).apply {
            putExtra("actorId", actorId)
        }
        viewModel.saveHistoryItem(actorId,"Actor")
        startActivity(intent)
    }

    override fun onTvShowClicked(tvShowId: Int) {
        val intent = Intent(this, TvShowDetailsActivity::class.java).apply {
            putExtra("tvShowId", tvShowId)
        }
        viewModel.saveHistoryItem(tvShowId,"TvShow")
        startActivity(intent)
    }

    override fun onMovieClicked(movieId: Int) {
        searchView.clearFocus()
        val intent = Intent(this, MovieDetailsActivity::class.java).apply {
            putExtra("movieId", movieId)
        }
        viewModel.saveHistoryItem(movieId,"Movie")
        startActivity(intent)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

    private val searchAdapter = SearchContentAdapter(this)
    private lateinit var db: HistoryDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySearchBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setUpToolbar()
        searchView.requestFocus()
        setUpObservers()
        setUpRecyclerView()
        setUpHistory()

        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun setUpHistory() {
        db = Room.databaseBuilder(
            applicationContext,
            HistoryDatabase::class.java, "history"
        ).fallbackToDestructiveMigration().build()
        thread {
            Log.d("History",db.historyDao().getAll().toString())
        }
        viewModel.setHistory(db)
    }

    private fun setUpRecyclerView() {
        searchContentRecycler.layoutManager = LinearLayoutManager(this)
        searchContentRecycler.adapter = searchAdapter
        searchContentRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.extendSearchPage(viewModel.lastLoadedPage + 1)
                }
            }
        })
    }

    private fun setUpToolbar() {
        setSupportActionBar(customToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setUpObservers() {

        viewModel.queryString.observe(this, Observer {
            viewModel.initSearch()
        })
        viewModel.searchedItems.observe(this, Observer {
            if (viewModel.lastLoadedPage == 1)
                searchAdapter.clearItems()
            it.forEach {
                searchAdapter.addItem(it)
            }
            searchAdapter.notifyDataSetChanged()
        })
        viewModel.resetTrigger.observe(this, Observer {
            searchContentRecycler.smoothScrollToPosition(0)
        })

    }
}
