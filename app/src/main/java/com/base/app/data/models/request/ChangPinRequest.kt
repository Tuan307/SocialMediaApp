package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

data class ChangPinRequest(
    @SerializedName("oldPin") val oldPin: String,
    @SerializedName("newPin") val newPin: String,
    @SerializedName("ConfirmNewPin") val ConfirmNewPin: String,
)