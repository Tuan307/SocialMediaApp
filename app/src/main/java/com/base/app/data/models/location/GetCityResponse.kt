package com.base.app.data.models.location

import com.base.app.data.models.user.Status
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 11/2/2023
 */
data class GetLocationResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: List<LocationModel>?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)

data class LocationModel(
    @SerializedName("cityId")
    val cityId: Long?,
    val cityName: String?,
    val description: String?,
    val tag: String?,
    val locationImages: List<LocationImage>?,
    val url: String?
)

data class LocationImage(
    val id: Long?,
    @SerializedName("imageUrl")
    val imageUrl: String?
)