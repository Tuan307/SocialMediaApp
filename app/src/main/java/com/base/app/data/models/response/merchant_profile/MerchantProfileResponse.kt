package com.bytepay.app.model.response.merchant_profile


import com.google.gson.annotations.SerializedName

data class MerchantProfileResponse(
    @SerializedName("merchant")
    var merchant: Merchant?,
    @SerializedName("merchantBalance")
    var merchantBalance: List<MerchantBalance>?
)