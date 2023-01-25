package com.bytepay.app.model.response.create_payment_link


import com.base.app.data.models.response.create_payment_link.Merchant
import com.google.gson.annotations.SerializedName

data class CreatePaymentLinkResponse(
    @SerializedName("code")
    var code: String?,
    @SerializedName("codeBill")
    var codeBill: String?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("currency")
    var currency: String?,
    @SerializedName("customerInfo")
    var customerInfo: String?,
    @SerializedName("dateActive")
    var dateActive: Any?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("expireDate")
    var expireDate: String?,
    @SerializedName("gateway")
    var gateway: Any?,
    @SerializedName("gatewayUrl")
    var gatewayUrl: Any?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("merchant")
    var merchant: Merchant?,
    @SerializedName("money")
    var money: Int?,
    @SerializedName("moneyDiscount")
    var moneyDiscount: Int?,
    @SerializedName("notExpire")
    var notExpire: Boolean?,
    @SerializedName("note")
    var note: String?,
    @SerializedName("paid")
    var paid: Any?,
    @SerializedName("paymentLinkType")
    var paymentLinkType: String?,
    @SerializedName("paymentPurpose")
    var paymentPurpose: String?,
    @SerializedName("personFee")
    var personFee: String?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("statusDetail")
    var statusDetail: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?,
    @SerializedName("url")
    var url: String?,
    @SerializedName("valueAddedTax")
    var valueAddedTax: Any?
)