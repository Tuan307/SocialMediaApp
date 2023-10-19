package com.base.app.ui.group.for_you.viewdata

import com.base.app.R

/**
 * @author tuanpham
 * @since 10/18/2023
 */
data class GroupYourViewData(
    override val id: String,
    val groups: List<GroupItemYourGroupViewData>,

    ) : GroupForYouViewData {
    override val layoutRes: Int
        get() = R.layout.layout_for_you_group_header
}

data class GroupItemYourGroupViewData(
    val groupName: String,
    val groupImage: String
)