package com.base.app.data.models.group.response

import com.base.app.data.models.user.User
import com.base.app.data.models.user.Status
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 10/11/2023
 */
data class CreateGroupResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: GroupData?,
    val pageCount: Long?,
    val page: Long?
)

data class GetAllGroupResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: List<GroupData>?,
    val pageCount: Long?,
    val page: Long?
)

data class GroupData(
    val id: Long?,
    val groupName: String?,
    val groupDescription: String?,
    @SerializedName("groupImageUrl")
    val groupImageUrl: String?,
    val groupCreatedAt: String?,
    val groupOwner: User?,
    val groupPrivacy: String?
)

data class GetGroupRequestResponse(
    val status: Status?,
    val data: List<RequestModel>?,
    val pageCount: Long?,
    val page: Long?
)

data class RequestModel(
    val id: Long?,
    val createdAt: String?,
    val type: String?,
    val message: String?,
    @SerializedName("requestUserId")
    val requestUserId: User?,
    @SerializedName("fromInvitedUserId")
    val fromInvitedUserId: User?,
    @SerializedName("groupId")
    val groupId: GroupData?
)