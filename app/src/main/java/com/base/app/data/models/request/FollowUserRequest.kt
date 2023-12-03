package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 12/3/2023
 */
data class FollowUserRequest(
    @SerializedName("targetId")
    val targetId: String,
    @SerializedName("sourceId")
    val sourceId: String,
    @SerializedName("createdAt")
    val createdAt: String,
)