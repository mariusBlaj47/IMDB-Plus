package com.marius.personalimdb.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.marius.personalimdb.data.model.Media
import com.marius.personalimdb.data.model.Photo
import com.marius.personalimdb.data.model.Video
import com.marius.personalimdb.helper.OpensYoutube
import com.marius.personalimdb.ui.ImageFragment

class ImagePagerAdapter(
    fragmentManager: FragmentManager,
    private val callback: OpensYoutube? = null
) : FragmentPagerAdapter(
    fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
), OpensYoutube {
    override fun onVideoClicked() {
        callback?.onVideoClicked()
    }

    private val imageList = mutableListOf<Media>()

    override fun getItem(position: Int): Fragment {
        val item = imageList[position]
        if (item is Photo)
            return ImageFragment(item.path)
        return ImageFragment(
            "https://img.youtube.com/vi/" + (item as Video).key + "/0.jpg",
            this
        )
    }

    override fun getCount(): Int {
        return imageList.size
    }

    fun addPhotos(photos: MutableList<Media>) {
        imageList.addAll(photos)
        notifyDataSetChanged()
    }
}