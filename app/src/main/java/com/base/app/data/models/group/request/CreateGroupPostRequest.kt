package com.base.app.data.models.group.request

import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 10/14/2023
 */
data class CreateGroupPostRequest(
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("imagesList")
    val imagesList: List<String>,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("createdAt")
    val createdAt: String,
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
    val question: String?,
    @SerializedName("groupId")
    val groupId: Long
)