package com.base.app.data.models.city

import com.base.app.data.models.dating_app.Status
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 11/2/2023
 */
data class GetCityResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: List<CityModel>?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)

data class CityModel(
    @SerializedName("cityId")
    val cityId: Long?,
    val cityName: String?,
    val description: String?,
    val tag: String?,
    val cityImages: List<CityImage>?
)

data class CityImage(
    val id: Long?,
    @SerializedName("imageUrl")
    val imageUrl: String?
)