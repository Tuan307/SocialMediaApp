package com.base.app.ui.main.fragment.explore.viewdata

/**
 * @author tuanpham
 * @since 10/28/2023
 */
data class ExploreViewData(
    val id: String,
    val title: String,
    val exploreItems: List<ExploreItemViewData>
)

data class ExploreItemViewData(
    val id: String,
    val name: String,
    val userName: String,
    val image: String,
    val type: Int,
    val groupMember: String?
)