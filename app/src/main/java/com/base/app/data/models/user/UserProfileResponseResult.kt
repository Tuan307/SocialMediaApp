package com.base.app.data.models.user

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 9/18/2023
 */
data class UserProfileResponseResult(
    @SerializedName("status")
    val status: Status,
    @SerializedName("data")
    val data: User?
)

data class UserUpdateProfileResponse(
    @SerializedName("status")
    val status: Status,
    @SerializedName("data")
    val data: User?,
    @SerializedName("pageCount")
    val pageCount: Long,
    @SerializedName("page")
    val page: Long
)