package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

data class CalculateFeeRequest(
    @SerializedName("money") val money: Long,
)