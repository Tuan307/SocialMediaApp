package com.base.app.data.models.response


import com.google.gson.annotations.SerializedName

data class NumberUnreadNotificationResponse(
    @SerializedName("count")
    var count: Int?,
    @SerializedName("statusCode")
    var statusCode: Int?
)