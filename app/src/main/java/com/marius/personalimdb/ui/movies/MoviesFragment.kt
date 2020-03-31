package com.marius.personalimdb.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.marius.personalimdb.R
import com.marius.personalimdb.ui.movies.category.popular.MoviesPopularFragment
import com.marius.personalimdb.ui.movies.category.top.MoviesTopFragment
import com.marius.personalimdb.ui.movies.category.trending.MoviesTrendingFragment
import com.marius.personalimdb.ui.movies.category.upcoming.MoviesUpcomingFragment
import kotlinx.android.synthetic.main.fragment_movies.*


class MoviesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
    }

    private fun setUpViewPager() {
        viewPager.adapter = ViewPagerAdapter(childFragmentManager, 4)
        viewPager.offscreenPageLimit = 3
        tabs.setupWithViewPager(viewPager)
    }


    internal inner class ViewPagerAdapter(manager: FragmentManager, _pageCount: Int) :
        FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private var pageCount = 0
        private var titles = arrayOf("Popular", "Upcoming", "Trending", "Top Rated")

        init {
            pageCount = _pageCount
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> MoviesPopularFragment()
                1 -> MoviesUpcomingFragment()
                2 -> MoviesTrendingFragment()
                else -> MoviesTopFragment()
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