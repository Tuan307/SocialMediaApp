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

data class GetGroupByGroupIdAndMemberIdResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: GetGroupData?,
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

data class GroupPostData(
    val content: List<GroupPostModel>,
    val pageable: Pageable,
    val last: Boolean,
    val totalElements: Long,
    val totalPages: Long,
    val size: Long,
    val number: Long,
    val sort: Sort,
    val first: Boolean,
    val numberOfElements: Long,
    val empty: Boolean
)

data class Pageable(
    val pageNumber: Long,
    val pageSize: Long,
    val sort: Sort,
    val offset: Long,
    val unpaged: Boolean,
    val paged: Boolean
)

data class Sort(
    val empty: Boolean,
    val unsorted: Boolean,
    val sorted: Boolean
)

data class GroupPostModel(
    @SerializedName("groupPostId")
    val groupPostId: String,
    val description: String,
    val groupPostContentItemList: List<GroupPostContentItemList>,
    @SerializedName("groupPostUserId")
    val groupPostUserId: DatingUser,
    @SerializedName("groupPostModelId")
    val groupPostModelId: GroupData,
    val createdAt: String,
    val checkInAddress: String,
    val checkInLatitude: Double,
    val checkInLongitude: Double,
    val type: String,
    @SerializedName("videoUrl")
    val videoUrl: String?,
    val question: String?
)

data class GroupPostContentItemList(
    val id: Long,
    @SerializedName("imageUrl")
    val imageUrl: String
)

data class GetAllPostByGroup(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: GroupPostData?,
    val pageCount: Long?,
    val page: Long?
)

data class AddPostByGroupResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: GroupPostModel?,
    val pageCount: Long?,
    val page: Long?
)
