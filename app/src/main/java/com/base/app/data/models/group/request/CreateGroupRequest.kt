package com.base.app.data.models.group.request

/**
 * @author tuanpham
 * @since 10/10/2023
 */
data class CreateGroupRequest(
    private val groupName: String,
    private val groupDescription: String,
    private val groupImage: String,
    private val groupCreatedAt: String,
    private val userId: String,
    private val privacy: String,
)