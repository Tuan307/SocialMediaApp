package com.base.app.data.models.response.notification


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("allUser")
    var allUser: Int?,
    @SerializedName("code")
    var code: String?,
    @SerializedName("content")
    var content: String?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("sendDate")
    var sendDate: String?,
    @SerializedName("statusNotification")
    var statusNotification: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?,
    @SerializedName("via")
    var via: String?
)