package com.marius.personalimdb.data.model

import com.google.gson.annotations.SerializedName

data class RequestToken(
    @SerializedName(value = "id") val id: Int? = null,
    @SerializedName(value = "success") val success: Boolean? = null,
    @SerializedName(value = "request_token") val value: String? = null,
    @SerializedName(value = "status_message") val message: String? = null,
    @SerializedName(value = "session_id") val sessionId: String? = null
)