package com.marius.personalimdb.data.model

import com.google.gson.annotations.SerializedName

data class Credit(
    @SerializedName(value = "media_type") val type: String,
    @SerializedName(value = "title") val title: String,
    @SerializedName(value = "vote_average") val votes: Float


)