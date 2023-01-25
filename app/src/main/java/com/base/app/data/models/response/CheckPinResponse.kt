package com.base.app.data.models.response


import com.google.gson.annotations.SerializedName

data class CheckPinResponse(
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?
)