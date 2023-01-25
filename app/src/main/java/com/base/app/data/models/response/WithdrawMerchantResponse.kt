package com.base.app.data.models.response


import com.google.gson.annotations.SerializedName

data class WithdrawMerchantResponse(
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
        @SerializedName("id")
        var id: String?,
        @SerializedName("createdAt")
        var createdAt: String?,
        @SerializedName("updatedAt")
        var updatedAt: String?,
        @SerializedName("deletedAt")
        var deletedAt: Any?,
        @SerializedName("money")
        var money: String?,
        @SerializedName("feeWithdraw")
        var feeWithdraw: Double?,
        @SerializedName("fixedFee")
        var fixedFee: Double?,
        @SerializedName("status")
        var status: String?,
        @SerializedName("dateConfirm")
        var dateConfirm: Any?,
        @SerializedName("dateRequest")
        var dateRequest: String?,
        @SerializedName("code")
        var code: String?,
        @SerializedName("reasonOther")
        var reasonOther: String?,
        @SerializedName("refundMoney")
        var refundMoney: Double?,
        @SerializedName("minusMoney")
        var minusMoney: Double?,
        @SerializedName("feeProgram")
        var feeProgram: String?,
        @SerializedName("merchantBank")
        var merchantBank: MerchantBank?
    ) {
        data class MerchantBank(
            @SerializedName("id")
            var id: String?,
            @SerializedName("createdAt")
            var createdAt: String?,
            @SerializedName("updatedAt")
            var updatedAt: String?,
            @SerializedName("deletedAt")
            var deletedAt: Any?,
            @SerializedName("holder")
            var holder: String?,
            @SerializedName("branch")
            var branch: String?,
            @SerializedName("number")
            var number: String?,
            @SerializedName("status")
            var status: Boolean?,
            @SerializedName("bank")
            var bank: Bank?
        ) {
            data class Bank(
                @SerializedName("id")
                var id: String?,
                @SerializedName("createdAt")
                var createdAt: String?,
                @SerializedName("updatedAt")
                var updatedAt: String?,
                @SerializedName("deletedAt")
                var deletedAt: Any?,
                @SerializedName("name")
                var name: String?,
                @SerializedName("code")
                var code: String?,
                @SerializedName("status")
                var status: String?,
                @SerializedName("gateway")
                var gateway: String?
            )
        }
    }
}