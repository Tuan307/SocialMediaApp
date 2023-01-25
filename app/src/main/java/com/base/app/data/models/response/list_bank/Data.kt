package com.base.app.data.models.response.list_bank


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Data(
    @SerializedName("bank")
    var bank: Bank? = null,
    @SerializedName("branch")
    var branch: String? = null,
    @SerializedName("createdAt")
    var createdAt: String? = null,
    @SerializedName("deletedAt")
    var deletedAt: Any? = null,
    @SerializedName("holder")
    var holder: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("number")
    var number: String? = null,
    @SerializedName("status")
    var status: Boolean? = null,
    @SerializedName("updatedAt")
    var updatedAt: String? = null
) : Serializable