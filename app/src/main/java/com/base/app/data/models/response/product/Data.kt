package com.base.app.data.models.response.product


import com.base.app.data.models.response.merchant.Avatar
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class Data : Serializable {
    @SerializedName("createdAt")
    var createdAt: String? = null

    @SerializedName("currency")
    var currency: String? = null

    @SerializedName("deletedAt")
    var deletedAt: Any? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("image")
    var image: Avatar? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("price")
    var price: String? = "0"

    @SerializedName("quantity")
    var quantity: Int? = 0

    @SerializedName("status")
    var status: String? = null

    @SerializedName("adminApprove")
    var adminApprove: String? = null

    @SerializedName("total")
    var total: String? = "0"

    @SerializedName("updatedAt")
    var updatedAt: String? = null

    @SerializedName("revenue")
    var revenue: String? = "0"

    @SerializedName("categoryData")
    var categoryData: Any? = null

    @SerializedName("category")
    var category: Category? = null
}