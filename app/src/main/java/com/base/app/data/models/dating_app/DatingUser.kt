package com.base.app.data.models.dating_app

import androidx.room.PrimaryKey
import com.base.app.data.models.interest.InterestModel
import com.google.gson.annotations.SerializedName

/**
 * @author tuanpham
 * @since 9/18/2023
 */
data class DatingUser(
    @SerializedName("userId")
    val userId: String?,
    val userName: String?,
    val fullName: String?,
    @SerializedName("imageUrl")
    val imageUrl: String?,
    val bio: String?,
    val email: String?,
    val latitude: Double?,
    val longitude: Double?,
    var hasChosen: Boolean?,
    val lastOnline: String?,
    val isBlock: Boolean?,
    val userInterestProfiles: List<UserInterestProfile>?
)

data class UserInterestProfile(
    val id: Long,
    val tourInterest: InterestModel
)