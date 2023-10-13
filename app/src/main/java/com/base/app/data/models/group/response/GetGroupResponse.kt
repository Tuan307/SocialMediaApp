package com.base.app.data.models.group.response

import com.base.app.data.models.dating_app.DatingUser
import com.base.app.data.models.dating_app.Status
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 10/12/2023
 */
data class GetGroupResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: List<GetGroupData>?,
    val pageCount: Long?,
    val page: Long?
)

data class GetGroupData(
    val id: Long,
    @SerializedName("groupModelId")
    val groupModelId: GroupData,
    @SerializedName("groupMemberUserId")
    val groupMemberUserId: DatingUser,
    val type: String
)