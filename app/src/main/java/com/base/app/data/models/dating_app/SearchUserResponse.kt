package com.base.app.data.models.dating_app

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 9/21/2023
 */
data class SearchUserResponse(
    @SerializedName("status")
    val status: Status,
    @SerializedName("data")
    val data: List<DatingUser>,
    @SerializedName("pageCount")
    val pageCount: Int,
    @SerializedName("page")
    val page: Int,
)