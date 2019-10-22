package com.marius.personalimdb.helper

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.marius.personalimdb.R

class Glider(private val view: View) {

    private val BASE_URL = "http://image.tmdb.org/t/p/w"

    fun loadImageFrom(url: String, dimension: Int, imageView: ImageView) {
        if (url == "unavailable") {
            Glide.with(view)
                .load(R.drawable.profile_pic)
                .centerCrop()
                .into(imageView)
        } else {
            Glide.with(view)
                .load(BASE_URL + dimension.toString() + url)
                .centerCrop()
                .into(imageView)
        }

    }
}