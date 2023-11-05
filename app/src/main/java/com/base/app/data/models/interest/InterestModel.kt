package com.base.app.data.models.interest

import com.base.app.data.models.dating_app.Status
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 11/5/2023
 */

data class InterestResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: List<InterestModel>?,
    val pageCount: Long?,
    val page: Long?
)

data class InterestModel(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    var isChosen: Boolean = false
)