package com.base.app.data.models.response.calculateFee


import com.base.app.data.models.response.list_bank.Data
import com.google.gson.annotations.SerializedName

data class CalculateFeeResponse(
    @SerializedName("fee")
    var fee: Fee?,
    @SerializedName("merchantBank")
    var merchantBank: Data?,
    @SerializedName("withdrawFeeProgram")
    var withdrawFeeProgram: String?
)