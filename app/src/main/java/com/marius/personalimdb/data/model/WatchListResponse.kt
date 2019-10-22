package com.marius.personalimdb.data.model

import com.google.gson.annotations.SerializedName

data class WatchListResponse(
    @SerializedName("status_message") val message: String
)