package com.marius.personalimdb.ui.watchlist.tv

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
import com.marius.personalimdb.adapter.TvShowAdapter
import com.marius.personalimdb.helper.OpensTvShowDetails
import com.marius.personalimdb.ui.tvShows.details.TvShowDetailsActivity
import kotlinx.android.synthetic.main.fragment_watchlist_tv_shows.*

class WatchListTvShowsFragment : Fragment(), OpensTvShowDetails {
    override fun onTvShowClicked(tvShowId: Int) {
        val intent = Intent(context, TvShowDetailsActivity::class.java).apply {
            putExtra("tvShowId", tvShowId)
        }
        startActivity(intent)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(WatchListTvShowsViewModel::class.java)
    }

    private val tvShowAdapter = TvShowAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_watchlist_tv_shows, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpObservers()
        setUpPullRefresh()
        viewModel.initTvShows()
    }

    private fun setUpPullRefresh() {
        pullToRefreshTv.setOnRefreshListener {
            tvShowAdapter.clearItems()
            viewModel.lastLoadedPage = 0
            viewModel.initTvShows()
            pullToRefreshTv.isRefreshing = false
        }
    }

    private fun setUpRecyclerView() {
        tvShowsWatchList.layoutManager = LinearLayoutManager(context)
        tvShowsWatchList.adapter = tvShowAdapter
        tvShowsWatchList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.getTvShows(viewModel.lastLoadedPage + 1)
                }
            }
        })
    }

    private fun setUpObservers() {
        viewModel.tvShowList.observe(this, Observer {
            tvShowAdapter.addItems(
                it.toMutableList(),
                viewModel.totalPages != viewModel.lastLoadedPage
            )
        })
    }
}