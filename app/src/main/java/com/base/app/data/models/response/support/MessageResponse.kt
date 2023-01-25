package com.base.app.data.models.response.support

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("id")
    var id: String?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?,
    @SerializedName("deletedAt")
    var deletedAt: String?,
    @SerializedName("content")
    var content: String?,
    @SerializedName("reply")
    var reply: String?,
    @SerializedName("status")
    var status: String?
)