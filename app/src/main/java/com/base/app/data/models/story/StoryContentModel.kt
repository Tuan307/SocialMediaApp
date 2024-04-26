package com.base.app.data.models.story

import com.base.app.data.models.response.post.Status
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 4/27/2024
 */
data class StoryContentModelResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: List<StoryContentModel>?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)

data class StoryContentModel(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("contentUrl")
    val contentUrl: String?
)