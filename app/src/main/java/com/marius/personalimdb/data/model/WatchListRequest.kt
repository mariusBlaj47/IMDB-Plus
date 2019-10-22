package com.marius.personalimdb.data.model

import com.google.gson.annotations.SerializedName

data class WatchListRequest(
    @SerializedName("media_id") val id: Int? = null,
    @SerializedName("watchlist") val watchlist: Boolean? = null,
    @SerializedName("media_type") val type: String? = null
)