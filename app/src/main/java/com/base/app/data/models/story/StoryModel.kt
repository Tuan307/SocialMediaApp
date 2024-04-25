package com.base.app.data.models.story

import com.base.app.data.models.response.post.Status
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 4/23/2024
 */
data class StoryModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("folderAvatar")
    val folderAvatar: String?,
)

data class StoryFolderResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: List<StoryModel>?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)

data class AddStoryFolderResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: StoryModel?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)

data class StoryImage(
    val id: Int?,
    val imageUrl: String?,
    val type: Int?,
    val createdAt: String?
)