package com.marius.personalimdb.data.model

import com.google.gson.annotations.SerializedName

sealed class Media

data class Photo(
    @SerializedName(value = "file_path") val path: String,
    @SerializedName(value = "aspect_ratio") val ratio: Float
) : Media()

data class Video(
    @SerializedName(value = "type") val type: String,
    @SerializedName(value = "site") val site: String,
    @SerializedName(value = "key") val key: String,
    @SerializedName(value = "name") val name: String
) : Media()