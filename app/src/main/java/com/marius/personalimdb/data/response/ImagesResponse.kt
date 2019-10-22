package com.marius.personalimdb.data.response

import com.google.gson.annotations.SerializedName
import com.marius.personalimdb.data.model.Photo


data class ImagesResponse(
    @SerializedName(value = "backdrops") val results: List<Photo>?,
    @SerializedName(value = "results") val posters: List<Photo>?
)