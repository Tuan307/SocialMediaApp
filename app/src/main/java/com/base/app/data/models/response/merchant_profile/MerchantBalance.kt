package com.bytepay.app.model.response.merchant_profile


import com.google.gson.annotations.SerializedName

data class MerchantBalance(
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("money")
    var money: Double?,
    @SerializedName("type")
    var type: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?
)