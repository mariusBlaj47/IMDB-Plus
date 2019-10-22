package com.marius.personalimdb.data.response

import com.google.gson.annotations.SerializedName
import com.marius.personalimdb.data.model.Movie

data class MovieCreditsResponse(
    @SerializedName("cast") val movies: List<Movie>
)