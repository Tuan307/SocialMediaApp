package com.bytepay.app.model.response.merchant_profile


import com.google.gson.annotations.SerializedName

data class MerchantBank(
    @SerializedName("branch")
    var branch: String?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("holder")
    var holder: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("number")
    var number: String?,
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("updatedAt")
    var updatedAt: String?
)