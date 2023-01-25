package com.base.app.data.models.response.merchant


import com.google.gson.annotations.SerializedName

open class Avatar : java.io.Serializable {
    @SerializedName("createdAt")
    var createdAt: String? = null

    @SerializedName("deletedAt")
    var deletedAt: Any? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("objectId")
    var objectId: String? = null

    @SerializedName("objectType")
    var objectType: String? = null

    @SerializedName("path")
    var path: String? = null

    @SerializedName("size")
    var size: Int? = null

    @SerializedName("updatedAt")
    var updatedAt: String? = null

    @SerializedName("userId")
    var userId: String? = null

    @SerializedName("userType")
    var userType: Any? = null
}