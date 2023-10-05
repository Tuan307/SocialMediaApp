package com.base.app.data.models.response.post

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 10/3/2023
 */
data class GetAllSavedPostResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: List<SavedPost>?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)

data class SavedPost(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("post_saved_id")
    val post_saved_id: PostContent?
)

data class CheckExistSavedPostResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: SavedPost?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)