package com.marius.personalimdb.helper


import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.marius.personalimdb.data.model.Movie
import com.marius.personalimdb.data.model.TvShow

fun Collection<Movie>.filterAll(): List<Movie> {
    return this.filter {
        !it.description.isNullOrEmpty()
    }.filter {
        !it.poster.isNullOrEmpty()
    }.filter {
        !it.releaseDate.isNullOrEmpty()
    }
}

fun Collection<TvShow>.filterAllTvShows(): List<TvShow> {
    return this.filter {
        !it.description.isNullOrEmpty()
    }.filter {
        !it.poster.isNullOrEmpty()
    }.filter {
        !it.firstAirDate.isNullOrEmpty()
    }
}

@BindingAdapter("goneUnless")
fun View.goneUnless(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("onSearch", "resetTrigger")
fun onSearch(
    searchView: SearchView,
    queryText: MutableLiveData<String>,
    trigger: MutableLiveData<Unit>
) {
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(p0: String?): Boolean {
            queryText.value = p0
            trigger.value = Unit
            return false
        }

        override fun onQueryTextSubmit(p0: String?): Boolean {
            searchView.clearFocus()
            return false
        }
    })
}

@BindingAdapter("imageSource", "imageDimension")
fun setImage(imageView: ImageView, url: String, dimension: Int) {
    val root = imageView.rootView
    Glider(root).loadImageFrom(url, dimension, imageView)
}

@BindingAdapter("profilePicSource", "dimension")
fun setProfilePic(imageView: ImageView, url: String?, dimension: Int) {
    val root = imageView.rootView
    if (url == null) {
        Glider(root).loadImageFrom("unavailable", dimension, imageView)
    } else {
        Glider(root).loadImageFrom(url, dimension, imageView)
    }
}