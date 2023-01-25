package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

data class WithdrawRequest(
    @SerializedName("money") val money: Long,
    @SerializedName("fee") val fee: Double,
    @SerializedName("merchantBankId") val merchantBankId: String,
    @SerializedName("feeProgram") val feeProgram: String,
)