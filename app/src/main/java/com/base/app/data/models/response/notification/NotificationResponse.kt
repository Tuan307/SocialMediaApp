package com.base.app.data.models.response.notification


import com.base.app.data.models.response.product.Meta
import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("data")
    var `data`: List<Data>?,
    @SerializedName("meta")
    var meta: Meta?
)