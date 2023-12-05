package com.base.app.data.models.chat

/**
 * @author tuanpham
 * @since 12/5/2023
 */
data class RecentChatModel(
    val targetName: String?,
    val targetImage: String?,
    val lastMessage: String?,
    val createdAt: String?,
    val targetId: String?
) {
    constructor() : this(null, null, null, null, null)
}