package com.base.app.data.models.response

import com.base.app.data.models.user.User
import com.base.app.data.models.user.Status
import com.base.app.data.models.group.response.GroupData
import com.base.app.data.models.response.post.Pageable
import com.base.app.data.models.response.post.Sort
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 10/4/2023
 */
data class NotificationResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: NotificationContent?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)

data class GetNotificationResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: NotificationData?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)

data class NotificationContent(
    val id: Long?,
    val isPost: Boolean?,
    val isInvitation: Boolean?,
    val text: String?,
    val isRequest: Boolean?,
    @SerializedName("notificationPostId")
    val notificationPostId: String?,
    val notificationTimeStamp: String?,
    @SerializedName("notificationOwnerId")
    val notificationOwnerId: String?,
    @SerializedName("notificationUserId")
    val notificationUserId: User?,
    @SerializedName("notificationGroupId")
    val notificationGroupId: GroupData?
)

data class NotificationData(
    val content: List<NotificationContent>?,
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