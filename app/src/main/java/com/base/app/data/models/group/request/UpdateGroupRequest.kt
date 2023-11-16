package com.base.app.data.models.group.request

/**
 * @author tuanpham
 * @since 11/16/2023
 */
data class UpdateGroupRequest(
    val groupName: String,
    val groupDescription: String,
    val groupImage: String,
    val privacy: String,
)