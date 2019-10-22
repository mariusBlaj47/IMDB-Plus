package com.marius.moviedatabase.retrofitApi.responses

import com.google.gson.annotations.SerializedName
import com.marius.personalimdb.data.model.Movie

data class SearchMovieResponse(
    @SerializedName(value = "results") val results: List<Movie>?,
    @SerializedName(value = "page") val currentPage: Int,
    @SerializedName(value = "total_pages") val totalPages: Int
)
