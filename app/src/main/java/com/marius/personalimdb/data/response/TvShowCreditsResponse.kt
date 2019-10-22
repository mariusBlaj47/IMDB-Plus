package com.marius.personalimdb.data.response

import com.google.gson.annotations.SerializedName
import com.marius.personalimdb.data.model.TvShow

data class TvShowCreditsResponse(
    @SerializedName("cast") val movies: List<TvShow>
)