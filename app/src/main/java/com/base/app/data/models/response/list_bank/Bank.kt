package com.base.app.data.models.response.list_bank


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Bank(
    @SerializedName("imageUrl")
    var imageUrl: String? = null,
    @SerializedName("code")
    var code: String? = null,
    @SerializedName("createdAt")
    var createdAt: String? = null,
    @SerializedName("deletedAt")
    var deletedAt: Any? = null,
    @SerializedName("gateway")
    var gateway: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("status")
    var status: String? = null,
    @SerializedName("updatedAt")
    var updatedAt: String? = null
) : Serializable