package com.base.app.data.models.response.calculateFee


import com.google.gson.annotations.SerializedName

data class Fee(
    @SerializedName("fixedFee")
    var fixedFee: Int?,
    @SerializedName("moneyWithdraw")
    var moneyWithdraw: Double?,
    @SerializedName("withdrawFee")
    var withdrawFee: Double?
)