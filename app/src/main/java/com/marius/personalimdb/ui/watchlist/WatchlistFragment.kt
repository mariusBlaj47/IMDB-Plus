package com.marius.personalimdb.ui.watchlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.marius.personalimdb.R
import com.marius.personalimdb.ui.watchlist.movies.WatchListMoviesFragment
import com.marius.personalimdb.ui.watchlist.tv.WatchListTvShowsFragment
import kotlinx.android.synthetic.main.fragment_watchlist.*

class WatchlistFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_watchlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
    }

    private fun setUpViewPager() {
        viewPager.adapter = ViewPagerAdapter(childFragmentManager, 2)
        viewPager.offscreenPageLimit = 1
        tabs.setupWithViewPager(viewPager)
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager, _pageCount: Int) :
        FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private var pageCount = 0
        private var titles = arrayOf("Movies", "Tv Shows")

        init {
            pageCount = _pageCount
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> WatchListMoviesFragment()
                else -> WatchListTvShowsFragment()
            }
        }

        override fun getCount(): Int {
            return pageCount
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }
}