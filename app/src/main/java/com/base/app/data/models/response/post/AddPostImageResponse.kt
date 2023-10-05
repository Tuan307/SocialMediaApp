package com.base.app.data.models.response.post

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 9/20/2023
 */
data class AddPostImageResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: PostContent?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)
