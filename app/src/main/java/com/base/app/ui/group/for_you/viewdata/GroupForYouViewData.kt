package com.base.app.ui.group.for_you.viewdata

import androidx.recyclerview.widget.DiffUtil.ItemCallback

/**
 * @author tuanpham
 * @since 10/18/2023
 */
interface GroupForYouViewData {
    val id: String
    val layoutRes: Int

    object GroupForYouDiffUtil : ItemCallback<GroupForYouViewData>() {
        override fun areItemsTheSame(
            oldItem: GroupForYouViewData,
            newItem: GroupForYouViewData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GroupForYouViewData,
            newItem: GroupForYouViewData
        ): Boolean {
            return oldItem.layoutRes == newItem.layoutRes
        }

    }
}