package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 4/25/2024
 */
data class AddStoryFolderRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("folderAvatar")
    val folderAvatar: String,
    @SerializedName("userId")
    val userId: String,
)