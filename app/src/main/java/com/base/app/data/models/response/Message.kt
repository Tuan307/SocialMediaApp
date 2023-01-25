package com.base.app.data.models.response

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message") val message: String
)