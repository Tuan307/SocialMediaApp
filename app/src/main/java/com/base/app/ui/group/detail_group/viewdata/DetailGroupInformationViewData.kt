package com.base.app.ui.group.detail_group.viewdata

import com.base.app.R

/**
 * @author tuanpham
 * @since 10/13/2023
 */
data class DetailGroupInformationViewData(
    override val id: String,
    val imageUrl: String,
    val userImageUrl: String,
    val groupName: String,
    val groupOwnerId: String,
    val groupPrivacy: String,
    val groupMemberNumber: String,
    var hasJoined: Boolean,
    var hasRequested: Boolean

) : DetailGroupViewData {
    override val layoutRes: Int
        get() = R.layout.layout_detail_group_information
}

data class DetailUpdateGroupInformationViewData(
    val groupName: String,
    val groupDescription: String,
    val groupImage: String,
    val privacy: String,
)