package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 9/20/2023
 */
data class PaginationRequest(
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("pageNumber")
    val pageNumber: Int
)