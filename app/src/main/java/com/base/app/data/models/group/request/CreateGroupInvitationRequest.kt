package com.base.app.data.models.group.request

/**
 * @author tuanpham
 * @since 10/12/2023
 */
data class CreateGroupInvitationRequest(
    val groupId: Long,
    val createdAt: String,
    val message: String,
    val userId: String,
    val type: String,
    val fromInvitedUserId: String,
)