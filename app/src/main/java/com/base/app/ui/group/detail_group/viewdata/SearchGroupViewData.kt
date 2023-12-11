package com.base.app.ui.group.detail_group.viewdata

import com.base.app.data.models.user.User
import com.base.app.data.models.group.response.GroupData
import com.base.app.data.models.response.post.ImagesList

/**
 * @author tuanpham
 * @since 10/17/2023
 */
data class SearchGroupViewData(
    val groupPostId: String,
    val description: String,
    val groupPostContentItemList: List<ImagesList>,
    val groupPostUser: User,
    val groupModel: GroupData,
    val createdAt: String,
    val checkInAddress: String,
    val checkInLatitude: Double,
    val checkInLongitude: Double,
    val type: String,
    val videoUrl: String?,
    val question: String?
)