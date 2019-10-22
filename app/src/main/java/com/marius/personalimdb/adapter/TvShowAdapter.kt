package com.marius.personalimdb.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.marius.personalimdb.R
import com.marius.personalimdb.data.model.TvShow
import com.marius.personalimdb.databinding.ItemTvShowBinding
import com.marius.personalimdb.helper.OpensTvShowDetails
import com.marius.personalimdb.ui.tvShows.ItemTvShowViewModel

class TvShowAdapter(private val callback: OpensTvShowDetails) :
    RecyclerView.Adapter<TvShowAdapter.ViewHolder>(), OpensTvShowDetails {
    override fun onTvShowClicked(tvShowId: Int) {
        callback.onTvShowClicked(tvShowId)
    }

    private val loadingItem =
        TvShow(-1, "1", "1", 1f, 1,"!", null, 1, 1, emptyList(), emptyList(), 1f, "", null)
    private val loading = 1
    private val tvShow = 0
    private val data = mutableListOf<TvShow>()

    init {
        data.add(loadingItem)
        notifyDataSetChanged()
    }

    fun clearItems() {
        data.clear()
        data.add(loadingItem)
        notifyDataSetChanged()
    }

    fun addItems(newData: MutableList<TvShow>, addLoading: Boolean = true) {
        if (data.isNotEmpty())
            data.removeAt(data.lastIndex)
        data.addAll(newData)
        if (addLoading) {
            data.add(loadingItem)
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position] == loadingItem)
            loading
        else tvShow
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            tvShow -> {
                TvShowVH(parent)
            }
            else -> {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_progress50dp, parent, false)
                LoadingVH(itemView)
            }
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder is TvShowVH)
            holder.bind(data[position])
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class LoadingVH(itemView: View) : TvShowAdapter.ViewHolder(itemView)

    inner class TvShowVH(
        private val parent: ViewGroup,
        private val binding: ItemTvShowBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_tv_show,
            parent,
            false
        )
    ) : ViewHolder(binding.root) {

        @SuppressLint("NewApi")
        fun bind(tvShow: TvShow) {
            binding.viewModel =
                ItemTvShowViewModel(tvShow, this@TvShowAdapter)
        }
    }
}