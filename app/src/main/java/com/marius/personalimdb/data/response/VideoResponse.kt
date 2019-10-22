package com.marius.personalimdb.data.response

import com.google.gson.annotations.SerializedName
import com.marius.personalimdb.data.model.Video

data class VideoResponse(
    @SerializedName(value = "results") val videos: List<Video>?
)