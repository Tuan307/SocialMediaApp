package com.base.app.data.models.response

import com.google.gson.annotations.SerializedName

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
    var expireDate: String?,
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