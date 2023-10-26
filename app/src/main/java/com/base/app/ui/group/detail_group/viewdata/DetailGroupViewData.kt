package com.base.app.ui.group.detail_group.viewdata

import androidx.recyclerview.widget.DiffUtil.ItemCallback

/**
 * @author tuanpham
 * @since 10/13/2023
 */
interface DetailGroupViewData {
    val id: String
    val layoutRes: Int

    object DetailGroupDiffUtil : ItemCallback<DetailGroupViewData>() {
        override fun areItemsTheSame(
            oldItem: DetailGroupViewData,
            newItem: DetailGroupViewData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DetailGroupViewData,
            newItem: DetailGroupViewData
        ): Boolean {
            return oldItem == newItem
        }

    }
}