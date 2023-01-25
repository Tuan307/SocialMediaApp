package com.base.app.data.models.response


import com.base.app.data.models.response.create_payment_link.Merchant
import com.base.app.data.models.response.list_bank.Data
import com.google.gson.annotations.SerializedName

data class WithdrawResponse(
    @SerializedName("code")
    var code: String?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("dateConfirm")
    var dateConfirm: Any?,
    @SerializedName("dateRequest")
    var dateRequest: String?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("feeProgram")
    var feeProgram: String?,
    @SerializedName("feeWithdraw")
    var feeWithdraw: Double?,
    @SerializedName("fixedFee")
    var fixedFee: Int?,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("merchant")
    var merchant: Merchant?,
    @SerializedName("merchantBank")
    var merchantBank: Data?,
    @SerializedName("minusMoney")
    var minusMoney: Int?,
    @SerializedName("money")
    var money: Int?,
    @SerializedName("reasonOther")
    var reasonOther: Any?,
    @SerializedName("refundMoney")
    var refundMoney: Int?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?
)