package com.marius.personalimdb.data.response

import com.google.gson.annotations.SerializedName
import com.marius.personalimdb.data.model.SearchItem

data class SearchResponse(
    @SerializedName(value = "results") val results: List<SearchItem>?,
    @SerializedName(value = "page") val currentPage: Int,
    @SerializedName(value = "total_pages") val totalPages: Int
)