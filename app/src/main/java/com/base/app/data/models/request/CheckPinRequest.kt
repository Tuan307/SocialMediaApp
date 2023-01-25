package com.base.app.data.models.request


import com.google.gson.annotations.SerializedName

data class CheckPinRequest(
    @SerializedName("pin")
    var pin: String?,
    @SerializedName("type")
    var type: String?,
    @SerializedName("withdrawId")
    var withdrawId: String? = null
)