package com.base.app.ui.group.your_group.viewdata

import com.base.app.R

/**
 * @author tuanpham
 * @since 10/12/2023
 */
data class GroupHeaderViewData(
    override val id: Long,
    val title: String
) : GroupViewData {
    override val layoutRes: Int
        get() = R.layout.layout_your_group_header
}