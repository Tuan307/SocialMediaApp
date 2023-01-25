package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

data class CreateSupportRequest(
    @SerializedName("content")
    var content: String?,
    @SerializedName("paymentLinkId")
    var paymentLinkId: String?
)