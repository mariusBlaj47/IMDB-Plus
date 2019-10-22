package com.marius.personalimdb.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.marius.personalimdb.R
import com.marius.personalimdb.helper.Glider
import com.marius.personalimdb.helper.OpensYoutube
import kotlinx.android.synthetic.main.item_photo.image
import kotlinx.android.synthetic.main.item_video.*

class ImageFragment(private val url: String, private val callback: OpensYoutube? = null) :
    Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (url.contains("youtube", true))
            inflater.inflate(R.layout.item_video, container, false)
        else inflater.inflate(R.layout.item_photo, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (url.contains("youtube", true)) {
            Glide.with(view)
                .load(url)
                .centerCrop()
                .into(image)
            video.setOnClickListener {
                callback?.onVideoClicked()
            }
        } else Glider(image).loadImageFrom(url, 780, image)
    }
}