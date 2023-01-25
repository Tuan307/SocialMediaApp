package com.base.app.data.models.response.product


import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class Category : Serializable {
    @SerializedName("createdAt")
    var createdAt: String? = null

    @SerializedName("deletedAt")
    var deletedAt: Any? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("list")
    var list: Any? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("status")
    var status: Boolean? = null

    @SerializedName("updatedAt")
    var updatedAt: String? = null
}