package com.base.app.data.models.dating_app

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 9/18/2023
 */
data class Status(
    @SerializedName("code")
    val code: Long,
    @SerializedName("message")
    val message: String
)