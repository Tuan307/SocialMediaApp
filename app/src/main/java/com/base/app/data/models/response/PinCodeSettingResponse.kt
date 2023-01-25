package com.base.app.data.models.response


import com.base.app.data.models.response.create_payment_link.Merchant
import com.google.gson.annotations.SerializedName

data class PinCodeSettingResponse(
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("merchant")
    var merchant: Merchant?,
    @SerializedName("updatedAt")
    var updatedAt: String?
)