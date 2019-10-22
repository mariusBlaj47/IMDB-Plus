package com.marius.personalimdb.data.model

import com.google.gson.annotations.SerializedName

data class ImdbRating(
    @SerializedName(value = "imdbRating") val rating: String,
    @SerializedName(value = "imdbVotes") val votes: String
)