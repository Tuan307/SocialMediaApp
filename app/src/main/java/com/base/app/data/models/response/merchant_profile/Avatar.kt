package com.bytepay.app.model.response.merchant_profile


import com.google.gson.annotations.SerializedName

data class Avatar(
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("objectId")
    var objectId: String?,
    @SerializedName("objectType")
    var objectType: String?,
    @SerializedName("path")
    var path: String?,
    @SerializedName("size")
    var size: Int?,
    @SerializedName("updatedAt")
    var updatedAt: String?,
    @SerializedName("userId")
    var userId: String?,
    @SerializedName("userType")
    var userType: Any?
)