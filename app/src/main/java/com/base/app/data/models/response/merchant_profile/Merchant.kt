package com.bytepay.app.model.response.merchant_profile


import com.google.gson.annotations.SerializedName

data class Merchant(
    @SerializedName("address")
    var address: String?,
    @SerializedName("avatar")
    var avatar: Avatar?,
    @SerializedName("birthday")
    var birthday: String?,
    @SerializedName("country")
    var country: Country?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("deletedAt")
    var deletedAt: Any?,
    @SerializedName("district")
    var district: District?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("firstName")
    var firstName: String?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("identityCard")
    var identityCard: String?,
    @SerializedName("isActive")
    var isActive: Int?,
    @SerializedName("lastName")
    var lastName: String?,
    @SerializedName("merchantBank")
    var merchantBank: List<MerchantBank>?,
    @SerializedName("mobile")
    var mobile: String?,
    @SerializedName("platform")
    var platform: String?,
    @SerializedName("province")
    var province: Province?,
    @SerializedName("referralCode")
    var referralCode: Any?,
    @SerializedName("status")
    var status: String?,
    @SerializedName("type")
    var type: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?,
    @SerializedName("ward")
    var ward: Any?
)