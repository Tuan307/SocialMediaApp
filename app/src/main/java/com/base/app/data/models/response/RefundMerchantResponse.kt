package com.base.app.data.models.response


import com.google.gson.annotations.SerializedName

data class RefundMerchantResponse(
    @SerializedName("meta")
    var meta: Meta?,
    @SerializedName("data")
    var `data`: List<Data?>?
) {
    data class Meta(
        @SerializedName("page")
        var page: Double?,
        @SerializedName("take")
        var take: Double?,
        @SerializedName("total")
        var total: Double?,
        @SerializedName("totalPage")
        var totalPage: Double?,
        @SerializedName("hasPreviousPage")
        var hasPreviousPage: Boolean?,
        @SerializedName("hasNextPage")
        var hasNextPage: Boolean?
    )

    data class Data(
        @SerializedName("id")
        var id: String?,
        @SerializedName("createdAt")
        var createdAt: String?,
        @SerializedName("updatedAt")
        var updatedAt: String?,
        @SerializedName("deletedAt")
        var deletedAt: Any?,
        @SerializedName("code")
        var code: String?,
        @SerializedName("fixedFee")
        var fixedFee: Double?,
        @SerializedName("fee")
        var fee: Double?,
        @SerializedName("personFee")
        var personFee: String?,
        @SerializedName("money")
        var money: Double?,
        @SerializedName("moneyMinus")
        var moneyMinus: Double?,
        @SerializedName("content")
        var content: String?,
        @SerializedName("status")
        var status: String?,
        @SerializedName("reasonOther")
        var reasonOther: Any?,
        @SerializedName("reasonRefuseOther")
        var reasonRefuseOther: Any?,
        @SerializedName("refundInfo")
        var refundInfo: Any?,
        @SerializedName("trace")
        var trace: Any?,
        @SerializedName("transaction")
        var transaction: Transaction?,
        @SerializedName("reason")
        var reason: Reason?,
        @SerializedName("reasonRefuse")
        var reasonRefuse: Any?
    ) {
        data class Transaction(
            @SerializedName("id")
            var id: String?,
            @SerializedName("createdAt")
            var createdAt: String?,
            @SerializedName("updatedAt")
            var updatedAt: String?,
            @SerializedName("deletedAt")
            var deletedAt: Any?,
            @SerializedName("code")
            var code: String?,
            @SerializedName("money")
            var money: Double?,
            @SerializedName("paymentInfo")
            var paymentInfo: String?,
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
            @SerializedName("statusDetail")
            var statusDetail: Any?,
            @SerializedName("moneyDiscount")
            var moneyDiscount: Double?,
            @SerializedName("paymentLink")
            var paymentLink: PaymentLink?
        ) {
            data class PaymentLink(
                @SerializedName("id")
                var id: String?,
                @SerializedName("createdAt")
                var createdAt: String?,
                @SerializedName("updatedAt")
                var updatedAt: String?,
                @SerializedName("deletedAt")
                var deletedAt: Any?,
                @SerializedName("code")
                var code: String?,
                @SerializedName("url")
                var url: String?,
                @SerializedName("currency")
                var currency: String?,
                @SerializedName("dateActive")
                var dateActive: Any?,
                @SerializedName("expireDate")
                var expireDate: Any?,
                @SerializedName("notExpire")
                var notExpire: Boolean?,
                @SerializedName("codeBill")
                var codeBill: String?,
                @SerializedName("money")
                var money: Double?,
                @SerializedName("valueAddedTax")
                var valueAddedTax: Any?,
                @SerializedName("paymentPurpose")
                var paymentPurpose: String?,
                @SerializedName("note")
                var note: Any?,
                @SerializedName("paid")
                var paid: Boolean?,
                @SerializedName("status")
                var status: String?,
                @SerializedName("paymentLinkType")
                var paymentLinkType: String?,
                @SerializedName("statusDetail")
                var statusDetail: String?,
                @SerializedName("gatewayUrl")
                var gatewayUrl: String?,
                @SerializedName("gateway")
                var gateway: String?,
                @SerializedName("customerInfo")
                var customerInfo: String?,
                @SerializedName("personFee")
                var personFee: String?,
                @SerializedName("moneyDiscount")
                var moneyDiscount: Double?
            )
        }

        data class Reason(
            @SerializedName("id")
            var id: String?,
            @SerializedName("createdAt")
            var createdAt: String?,
            @SerializedName("updatedAt")
            var updatedAt: String?,
            @SerializedName("deletedAt")
            var deletedAt: Any?,
            @SerializedName("code")
            var code: String?,
            @SerializedName("content")
            var content: String?,
            @SerializedName("type")
            var type: String?,
            @SerializedName("status")
            var status: String?
        )
    }
}