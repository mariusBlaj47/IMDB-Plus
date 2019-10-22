package com.marius.personalimdb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.marius.personalimdb.R
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.databinding.ItemTvShowSmallBinding
import com.marius.personalimdb.helper.OpensTvShowDetails
import com.marius.personalimdb.ui.tvShows.ItemTvShowViewModel

class TvShowSmallAdapter(private val callback: OpensTvShowDetails) :
    RecyclerView.Adapter<TvShowSmallAdapter.TvShowVH>(), OpensTvShowDetails {

    override fun onTvShowClicked(tvShowId: Int) {
        callback.onTvShowClicked(tvShowId)
    }

    private val tvShowList = mutableListOf<TvShow>()

    fun addTvShows(tvShows: List<TvShow>) {
        tvShowList.addAll(tvShows)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowVH {
        return TvShowVH(parent)
    }

    override fun getItemCount(): Int = tvShowList.size

    override fun onBindViewHolder(holder: TvShowVH, position: Int) {
        holder.bind(tvShowList[position])
    }

    inner class TvShowVH(
        private val parent: ViewGroup,
        private val binding: ItemTvShowSmallBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_tv_show_small,
            parent,
            false
        )
    ) : TvShowAdapter.ViewHolder(binding.root) {
        fun bind(tvShow: TvShow) {
            binding.viewModel =
                ItemTvShowViewModel(tvShow, this@TvShowSmallAdapter)
        }
    }
}