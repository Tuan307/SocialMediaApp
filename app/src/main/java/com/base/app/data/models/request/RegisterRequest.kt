package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

open class RegisterRequest {

    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("password")
    var password: String? = null

    @SerializedName("mobile")
    var mobile: String? = null

    @SerializedName("referralCode")
    var referralCode: String? = null

    @SerializedName("platform")
    var platform: String? = null
}