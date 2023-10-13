package com.base.app.ui.group.your_group.viewdata

import androidx.recyclerview.widget.DiffUtil.ItemCallback

/**
 * @author tuanpham
 * @since 10/12/2023
 */
interface GroupViewData {
    val id: Long
    val layoutRes: Int

    object GroupViewDataDiffUtil : ItemCallback<GroupViewData>() {
        override fun areItemsTheSame(oldItem: GroupViewData, newItem: GroupViewData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GroupViewData, newItem: GroupViewData): Boolean {
            return oldItem.layoutRes == newItem.layoutRes
        }

    }
}