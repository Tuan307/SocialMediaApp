package com.base.app.data.models.recommend

import com.base.app.data.models.location.LocationImage
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 11/9/2023
 */

data class RecommendResponse(
    @SerializedName("recommended_cities")
    val recommended_cities: List<RecommendDataModel>?
)

data class RecommendDataModel(
    @SerializedName("city_id")
    val city_id: Long?,
    @SerializedName("city_name")
    val city_name: String?,
    val description: String?,
    val tag: String?,
    @SerializedName("image_url")
    val image_url: List<LocationImage>?,
    val url: String?,
    @SerializedName("similarity_score")
    val similarity_score: String?
)