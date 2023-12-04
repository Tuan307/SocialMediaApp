package com.base.app.data.models.response

import com.base.app.data.models.dating_app.DatingUser
import com.base.app.data.models.dating_app.Status
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
    val sourceId: DatingUser?,
    @SerializedName("targetId")
    val targetId: DatingUser?
)