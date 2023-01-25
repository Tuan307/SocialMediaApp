package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

data class CheckOTPRequest(
    @SerializedName("otp")
    var otp: String?,
    @SerializedName("type")
    var type: String = "pin-code"
)