package com.base.app.data.models.response.support


import com.base.app.data.models.response.PaymentLink
import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("content")
    var content: String?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("reply")
    var reply: Any?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?,
    @SerializedName("paymentLink")
    var paymentLink: PaymentLink?
)