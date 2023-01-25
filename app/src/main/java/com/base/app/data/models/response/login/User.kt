package com.base.app.data.models.response.login


import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User : RealmObject() {
    @PrimaryKey
    @SerializedName("id")
    var id: String? = null

    @SerializedName("address")
    var address: String? = null

    @SerializedName("createdAt")
    var createdAt: String? = null

    @SerializedName("deletedAt")
    var deletedAt: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("identityCard")
    var identityCard: String? = null

    @SerializedName("isActive")
    var isActive: Int? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("mobile")
    var mobile: String? = null

    @SerializedName("money")
    var money: Double? = 0.0

    @SerializedName("platform")
    var platform: String? = null

    @SerializedName("referralCode")
    var referralCode: String? = null

    @SerializedName("status")
    var status: String? = null

    @SerializedName("type")
    var type: String? = null

    @SerializedName("updatedAt")
    var updatedAt: String? = null

    @SerializedName("message")
    var message: String? = null

}