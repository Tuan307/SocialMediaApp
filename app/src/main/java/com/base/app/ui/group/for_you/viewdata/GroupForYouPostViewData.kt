package com.base.app.ui.group.for_you.viewdata

import com.base.app.R
import com.base.app.data.models.user.User
import com.base.app.data.models.group.response.GroupData
import com.base.app.data.models.response.post.ImagesList

/**
 * @author tuanpham
 * @since 10/18/2023
 */
data class GroupForYouPostViewData(
    override val id: String,
    val description: String,
    val itemList: List<ImagesList>,
    val postUser: User,
    val postGroup: GroupData,
    val createdAt: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val type: String,
    val videoUrl: String?,
    val question: String?

) : GroupForYouViewData {
    override val layoutRes: Int
        get() = R.layout.layout_for_you_group_body
}