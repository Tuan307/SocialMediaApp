package com.base.app.data.models.request

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 9/20/2023
 */
data class PostNewsFeedRequest(
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("imagesList")
    val imagesList: List<String>,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("checkInTimestamp")
    val checkInTimestamp: String,
    @SerializedName("checkInAddress")
    val checkInAddress: String,
    @SerializedName("checkInLatitude")
    val checkInLatitude: Double,
    @SerializedName("checkInLongitude")
    val checkInLongitude: Double,
    @SerializedName("type")
    val type: String,
    @SerializedName("videoUrl")
    val videoUrl: String?,
    @SerializedName("question")
    val question: String?
)