package com.base.app.data.models.response

import com.base.app.data.models.response.merchant.MerchantInfo
import com.google.gson.annotations.SerializedName

data class CreateSupportRequestResponse(
    @SerializedName("content")
    var content: String?,
    @SerializedName("merchant")
    var merchant: MerchantInfo?,
    @SerializedName("paymentLink")
    var paymentLink: PaymentLink?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("reply")
    var reply: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?,
    @SerializedName("status")
    var status: String?
)