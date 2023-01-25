package com.bytepay.app.model.response.merchant_profile


import com.google.gson.annotations.SerializedName

data class District(
    @SerializedName("code")
    var code: String?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?
)