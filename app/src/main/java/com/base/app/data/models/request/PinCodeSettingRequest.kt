package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

data class PinCodeSettingRequest(
    @SerializedName("pin") val pin: String,
    @SerializedName("confirmPin") val confirmPin: String
)