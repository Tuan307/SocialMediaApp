package com.base.app.data.models.response


import com.google.gson.annotations.SerializedName

data class SendOTPPinCodeResponse(
    @SerializedName("createdAt")
    var createdAt: String? = null,
    @SerializedName("deletedAt")
    var deletedAt: Any? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("mobile")
    var mobile: String? = null,
    @SerializedName("otp")
    var otp: String? = null,
    @SerializedName("price")
    var price: Int? = null,
    @SerializedName("status")
    var status: Boolean? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("updatedAt")
    var updatedAt: String? = null,
    @SerializedName("withdrawId")
    var withdrawId: Any? = null
)