package com.base.app.data.models.request

/**
 * @author tuanpham
 * @since 10/20/2023
 */
data class UpdateProfileRequest(
    val userId: String,
    val userName: String,
    val fullName: String,
    val bio: String,
    val imageUrl: String
)