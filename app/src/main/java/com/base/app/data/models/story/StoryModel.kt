package com.base.app.data.models.story

/**
 * @author tuanpham
 * @since 4/23/2024
 */
data class StoryModel(
    val id: Int?,
    val name: String?,
    val createdAt: String?,
    val imageUrl: String?
)

data class StoryImage(
    val id: Int?,
    val imageUrl: String?,
    val type: Int?,
    val createdAt: String?
)