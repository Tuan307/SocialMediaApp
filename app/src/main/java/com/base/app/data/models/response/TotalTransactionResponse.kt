package com.base.app.data.models.response


import com.google.gson.annotations.SerializedName

data class TotalTransactionResponse(
    @SerializedName("totalTransaction")
    var totalTransaction: Double?,
    @SerializedName("totalWithdraw")
    var totalWithdraw: Double?
)