package com.base.app.ui.group.detail_group.viewdata

import com.base.app.R
import com.base.app.data.models.response.post.ImagesList

/**
 * @author tuanpham
 * @since 10/13/2023
 */
data class DetailGroupPostViewData(
    override val id: String,
    val groupPostId: String,
    val description: String,
    val imagesList: List<String>?,
    val createdAt: String,
    val checkInAddress: String,
    val checkInLatitude: Double,
    val checkInLongitude: Double,
    val type: String,
    val videoUrl: String?,
    val question: String?,
) : DetailGroupViewData {
    override val layoutRes: Int
        get() = R.layout.layout_home_adapter
}