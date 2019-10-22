package com.marius.personalimdb.data.response

import com.google.gson.annotations.SerializedName
import com.marius.personalimdb.data.model.TvShow

data class SearchTvShowResponse(
    @SerializedName(value = "results") val results: List<TvShow>?,
    @SerializedName(value = "page") val currentPage: Int,
    @SerializedName(value = "total_pages") val totalPages: Int
)