package com.marius.personalimdb.data.model

import com.google.gson.annotations.SerializedName

data class ExternalLinks(
    @SerializedName(value = "imdb_id") val imdbId: String?
)