package com.base.app.data.models.interest.request

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 11/6/2023
 */
data class AddUserInterestRequest(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("interests")
    val interests: List<InterestModelRequest>
)

data class InterestModelRequest(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
)