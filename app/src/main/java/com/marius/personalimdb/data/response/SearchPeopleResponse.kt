package com.marius.personalimdb.data.response

import com.google.gson.annotations.SerializedName
import com.marius.personalimdb.data.model.Actor

data class SearchPeopleResponse(
    @SerializedName(value = "results") val results: List<Actor>?,
    @SerializedName(value = "page") val currentPage: Int,
    @SerializedName(value = "total_pages") val totalPages: Int
)