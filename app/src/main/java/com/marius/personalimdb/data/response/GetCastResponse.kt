package com.marius.moviedatabase.retrofitApi.responses

import com.google.gson.annotations.SerializedName
import com.marius.personalimdb.data.model.Actor

data class GetCastResponse(
    @SerializedName(value = "cast") val results: List<Actor>?
)