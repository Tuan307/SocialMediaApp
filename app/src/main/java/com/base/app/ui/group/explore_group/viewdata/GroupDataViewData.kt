package com.base.app.ui.group.explore_group.viewdata

import com.base.app.data.models.dating_app.DatingUser

/**
 * @author tuanpham
 * @since 10/19/2023
 */
data class GroupDataViewData(
    val id: Long,
    val groupName: String,
    val groupDescription: String,
    val groupImageUrl: String,
    val groupCreatedAt: String,
    val groupOwner: DatingUser?,
    val groupPrivacy: String,
    var hasJoined: Boolean = false,
)