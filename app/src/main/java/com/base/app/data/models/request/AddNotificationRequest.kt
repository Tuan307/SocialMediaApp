package com.base.app.data.models.request

/**
 * @author tuanpham
 * @since 10/4/2023
 */
data class AddNotificationRequest(
    val isPost: Boolean,
    val isInvitation: Boolean,
    val text: String,
    val ownerId: String,
    val postId: String,
    val timeStamp: String,
    val userId: String,
)