package com.base.app.data.models.response.merchant


import com.base.app.data.models.response.login.User
import com.google.gson.annotations.SerializedName

data class MerchantInfo(
    @SerializedName("avatar")
    var avatar: Avatar?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("merchant")
    var merchant: User?,
    @SerializedName("mobile")
    var mobile: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("status")
    var status: Boolean?,
    @SerializedName("updatedAt")
    var updatedAt: String?,
    @SerializedName("url")
    var url: String?
)