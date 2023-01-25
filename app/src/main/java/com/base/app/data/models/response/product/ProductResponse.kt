package com.base.app.data.models.response.product


import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("data")
    var `data`: List<Data?>? = null,
    @SerializedName("meta")
    var meta: Meta? = null,
    @SerializedName("message")
    var message: String? = null
)