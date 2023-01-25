package com.base.app.data.models.response.create_payment_link


import com.google.gson.annotations.SerializedName

data class Merchant(
    @SerializedName("address")
    var address: String?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("firstName")
    var firstName: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("identityCard")
    var identityCard: Any?,
    @SerializedName("isActive")
    var isActive: Int?,
    @SerializedName("lastName")
    var lastName: String?,
    @SerializedName("mobile")
    var mobile: String?,
    @SerializedName("money")
    var money: Double?,
    @SerializedName("platform")
    var platform: String?,
    @SerializedName("referralCode")
    var referralCode: Any?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("type")
    var type: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?
)