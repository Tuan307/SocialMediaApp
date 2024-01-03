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
    val data: LocationData?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)

data class LocationData(
    val content: List<LocationModel>?,
    val pageable: Pageable,
    val last: Boolean,
    val totalPages: Long,
    val totalElements: Long,
    val size: Long,
    val number: Long,
    val sort: Sort,
    val first: Boolean,
    val numberOfElements: Long,
    val empty: Boolean
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

data class Pageable(
    val pageNumber: Long,
    val pageSize: Long,
    val sort: Sort,
    val offset: Long,
    val paged: Boolean,
    val unpaged: Boolean
)

data class Sort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)
