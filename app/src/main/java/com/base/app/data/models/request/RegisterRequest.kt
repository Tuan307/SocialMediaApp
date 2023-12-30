package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 9/18/2023
 */
data class RegisterRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("fullName")
    val fullName: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("bio")
    val bio: String,
    @SerializedName("email")
    val email: String
)