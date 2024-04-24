package com.base.app.data.models.response.post

import android.os.Parcelable
import com.base.app.data.models.user.Status
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author tuanpham
 * @since 10/20/2023
 */
data class PostResponse(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: ImagePostData?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)

data class ImageUserProfilePost(
    @SerializedName("status")
    val status: Status?,
    @SerializedName("data")
    val data: List<PostContent>?,
    @SerializedName("pageCount")
    val pageCount: Long?,
    @SerializedName("page")
    val page: Long?
)

data class ImagePostData(
    val content: List<PostContent>,
    val pageable: Pageable,
    val totalPages: Long,
    val totalElements: Long,
    val last: Boolean,
    val size: Long,
    val number: Long,
    val sort: Sort,
    val first: Boolean,
    val numberOfElements: Long,
    val empty: Boolean
)

data class PostContent(
    @SerializedName("postId")
    val postId: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("imagesList")
    val imagesList: List<ImagesList>?,
    @SerializedName("checkInTimestamp")
    val checkInTimestamp: String?,
    @SerializedName("checkInAddress")
    val checkInAddress: String?,
    @SerializedName("checkInLatitude")
    val checkInLatitude: Double?,
    @SerializedName("checkInLongitude")
    val checkInLongitude: Double?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("question")
    val question: String?,
    @SerializedName("videoUrl")
    val videoUrl: String?,
    @SerializedName("postUserId")
    val postUserId: PostUserID?,
)

data class PostUserID(
    @SerializedName("userId")
    val userId: String?,
    @SerializedName("userName")
    val userName: String?,
    @SerializedName("fullName")
    val fullName: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("latitude")
    val latitude: Double?,
    @SerializedName("longitude")
    val longitude: Double?,
    @SerializedName("postItemList")
    val postItemList: Any?,
    @SerializedName("lastOnline")
    val lastOnline: String?,
    @SerializedName("isBlock")
    val isBlock: Boolean?
)

@Parcelize
data class ImagesList(
    val id: Long?,
    @SerializedName("imageUrl")
    val imageUrl: String?
) : Parcelable

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

data class Status(
    val code: Long,
    val message: String
)