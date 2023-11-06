package com.base.app.data.models.interest.response

import com.base.app.data.models.dating_app.Status
import com.base.app.data.models.interest.request.InterestModelRequest
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 11/7/2023
 */
data class AddInterestResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: List<AddInterestDataResponse>?,
    val pageCount: Long?,
    val page: Long?
)

data class AddInterestDataResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("tourInterest")
    val tourInterest: InterestModelRequest?,
)