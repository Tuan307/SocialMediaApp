package com.base.app.data.models.user

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 9/21/2023
 */
data class BaseApiResponse(
    @SerializedName("status")
    val status: Status,
    @SerializedName("data")
    val data: Any?,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("page")
    val page: Int,
)