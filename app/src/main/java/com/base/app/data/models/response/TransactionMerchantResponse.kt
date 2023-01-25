package com.base.app.data.models.response


import com.google.gson.annotations.SerializedName

data class TransactionMerchantResponse(
    @SerializedName("meta")
    var meta: Meta?,
    @SerializedName("data")
    var `data`: List<Data?>?
) {
    data class Meta(
        @SerializedName("page")
        var page: Int?,
        @SerializedName("take")
        var take: Int?,
        @SerializedName("total")
        var total: Int?,
        @SerializedName("totalPage")
        var totalPage: Int?,
        @SerializedName("hasPreviousPage")
        var hasPreviousPage: Boolean?,
        @SerializedName("hasNextPage")
        var hasNextPage: Boolean?
    )

    data class Data(
        @SerializedName("codeBill")
        var codeBill: String?,
        @SerializedName("id")
        var id: String?,
        @SerializedName("transactionId")
        var transactionId: String?,
        @SerializedName("createdAt")
        var createdAt: String?,
        @SerializedName("updatedAt")
        var updatedAt: String?,
        @SerializedName("deletedAt")
        var deletedAt: Any?,
        @SerializedName("transactionCreatedAt")
        var transactionCreatedAt: String?,
        @SerializedName("code")
        var code: String?,
        @SerializedName("money")
        var money: Double?,
        @SerializedName("payment_purpose")
        var paymentPurpose: String?,
        @SerializedName("paymentInfo")
        var paymentInfo: String?,
        @SerializedName("url")
        var url: String?,
        @SerializedName("expireDate")
        var expireDate: String?,
        @SerializedName("notExpire")
        var notExpire: Int?,
        @SerializedName("statusDetail")
        var statusDetail: String?,
        @SerializedName("note")
        var note: Any?,
        @SerializedName("gateway")
        var gateway: String?,
        @SerializedName("status")
        var status: String?,
        @SerializedName("fee")
        var fee: Double?,
        @SerializedName("plusMoney")
        var plusMoney: Double?,
        @SerializedName("fixedFee")
        var fixedFee: Double?,
        @SerializedName("moneyDiscount")
        var moneyDiscount: Double?,
        @SerializedName("paymentLink")
        var paymentLink: PaymentLink?
    )
}