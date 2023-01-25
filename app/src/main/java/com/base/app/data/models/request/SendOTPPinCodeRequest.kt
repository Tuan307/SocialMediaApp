package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

data class SendOTPPinCodeRequest(
    @SerializedName("reOtp")
    var reOtp: Boolean? = true,

    @SerializedName("type")
    var type: String? = "pin-code"

)
