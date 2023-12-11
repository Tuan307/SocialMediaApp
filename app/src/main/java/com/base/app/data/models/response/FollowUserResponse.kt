package com.base.app.data.models.response

import com.base.app.data.models.user.User
import com.base.app.data.models.user.Status
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 12/3/2023
 */
data class FollowUserResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: UserFollow?,
    val pageCount: Long?,
    val page: Long?
)

data class ListFollowResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: List<UserFollow>?,
    val pageCount: Long?,
    val page: Long?
)

data class UnFollowUserResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: Boolean?,
    val pageCount: Long?,
    val page: Long?
)

data class UserFollow(
    val id: Long?,
    val type: String?,
    val createdAt: String?,
    @SerializedName("sourceId")
    val sourceId: User?,
    @SerializedName("targetId")
    val targetId: User?
)