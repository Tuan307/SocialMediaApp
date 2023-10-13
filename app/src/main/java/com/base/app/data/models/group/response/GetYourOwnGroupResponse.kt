package com.base.app.data.models.group.response

import com.base.app.data.models.dating_app.Status
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 10/12/2023
 */
data class GetYourOwnGroupResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: List<GroupData>?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)