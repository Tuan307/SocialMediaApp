package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 4/27/2024
 */
data class AddStoryContentRequest(
    @SerializedName("type")
    val type: String,
    @SerializedName("storyId")
    val storyId: Int,
    @SerializedName("contentUrl")
    val contentUrl: List<String>
)