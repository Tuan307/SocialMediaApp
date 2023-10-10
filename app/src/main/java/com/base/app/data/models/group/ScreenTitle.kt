package com.base.app.data.models.group

import androidx.recyclerview.widget.DiffUtil.ItemCallback

/**
 * @author tuanpham
 * @since 10/10/2023
 */
data class ScreenTitle(
    val title: String,
    var isCheck: Boolean
) {
    object ScreenTitleDiffUtil : ItemCallback<ScreenTitle>() {
        override fun areItemsTheSame(oldItem: ScreenTitle, newItem: ScreenTitle): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ScreenTitle, newItem: ScreenTitle): Boolean {
            return oldItem == newItem
        }

    }
}