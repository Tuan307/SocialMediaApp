package com.base.app.data.models.group.response

import com.base.app.data.models.dating_app.DatingUser
import com.base.app.data.models.dating_app.Status
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 10/12/2023
 */
data class CreateInvitationResponse(
    @SerializedName("status")
    val status: Status,
    @SerializedName("data")
    val data: InvitationData?,
    @SerializedName("pageCount")
    val pageCount: Long,
    @SerializedName("page")
    val page: Long
)

data class InvitationData(
    val id: Long,
    val createdAt: String,
    val type: String,

    @SerializedName("requestUserId")
    val requestUserId: DatingUser,

    @SerializedName("groupId")
    val groupId: GroupData
)