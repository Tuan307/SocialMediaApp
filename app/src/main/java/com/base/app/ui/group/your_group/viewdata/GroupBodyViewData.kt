package com.base.app.ui.group.your_group.viewdata

import com.base.app.R

/**
 * @author tuanpham
 * @since 10/12/2023
 */
class GroupBodyViewData(
    override val id: Long,
    val groupName: String,
    val groupImage: String,
    val groupJoinedDate: String
) : GroupViewData {
    override val layoutRes: Int
        get() = R.layout.layout_your_group_body
}