package com.base.app.data.apis

import com.base.app.data.models.group.request.CreateGroupInvitationRequest
import com.base.app.data.models.group.request.CreateGroupPostRequest
import com.base.app.data.models.group.request.CreateGroupRequest
import com.base.app.data.models.group.request.JoinGroupRequest
import com.base.app.data.models.group.request.UpdateGroupRequest
import com.base.app.data.models.group.response.AddPostByGroupResponse
import com.base.app.data.models.group.response.CheckIfRequestToJoinGroupResponse
import com.base.app.data.models.group.response.CreateGroupResponse
import com.base.app.data.models.group.response.CreateInvitationResponse
import com.base.app.data.models.group.response.GetAllGroupResponse
import com.base.app.data.models.group.response.GetAllPostByGroup
import com.base.app.data.models.group.response.GetGroupByGroupIdAndMemberIdResponse
import com.base.app.data.models.group.response.GetGroupRequestResponse
import com.base.app.data.models.group.response.GetGroupResponse
import com.base.app.data.models.group.response.GetYourOwnGroupResponse
import com.base.app.data.models.group.response.SearchPostInGroupResponse
import com.base.app.data.models.interest.InterestResponse
import com.base.app.data.models.interest.UpdateInterestResponse
import com.base.app.data.models.interest.request.AddUserInterestRequest
import com.base.app.data.models.interest.response.AddInterestResponse
import com.base.app.data.models.location.GetLocationResponse
import com.base.app.data.models.request.AddNotificationRequest
import com.base.app.data.models.request.AddStoryFolderRequest
import com.base.app.data.models.request.FollowUserRequest
import com.base.app.data.models.request.PostNewsFeedRequest
import com.base.app.data.models.request.RegisterRequest
import com.base.app.data.models.request.SavedPostRequest
import com.base.app.data.models.request.UpdateProfileRequest
import com.base.app.data.models.response.FollowUserResponse
import com.base.app.data.models.response.GetNotificationResponse
import com.base.app.data.models.response.ListFollowResponse
import com.base.app.data.models.response.NotificationResponse
import com.base.app.data.models.response.UnFollowUserResponse
import com.base.app.data.models.response.post.AddPostImageResponse
import com.base.app.data.models.response.post.CheckExistSavedPostResponse
import com.base.app.data.models.response.post.GetAllSavedPostResponse
import com.base.app.data.models.response.post.ImageUserProfilePost
import com.base.app.data.models.response.post.PostResponse
import com.base.app.data.models.response.post.SavedPostResponse
import com.base.app.data.models.story.AddStoryFolderResponse
import com.base.app.data.models.story.StoryFolderResponse
import com.base.app.data.models.user.BaseApiResponse
import com.base.app.data.models.user.SearchUserResponse
import com.base.app.data.models.user.User
import com.base.app.data.models.user.UserProfileResponseResult
import com.base.app.data.models.user.UserUpdateProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author tuanpham
 * @since 9/18/2023
 */
interface APIService {

    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") id: String): Response<UserProfileResponseResult>

    @PUT("users/update/profile")
    suspend fun updateUserProfile(@Body request: UpdateProfileRequest): Response<UserUpdateProfileResponse>

    @POST("users/register")
    suspend fun registerUser(@Body user: RegisterRequest): Response<UserProfileResponseResult>

    @GET("users/search")
    suspend fun searchUsers(
        @Query("keyword") name: String, @Query("pageCount") pageCount: Int,
        @Query("pageNumber") pageNumber: Int
    ): Response<SearchUserResponse>

    @GET("users/nearby")
    suspend fun getAllNearbyUsers(
        @Query("userId") userId: String, @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double, @Query("limit") limit: Int
    ): Response<List<User>>

    @GET("users/interest")
    suspend fun getAllInterests(
    ): Response<InterestResponse>

    @GET("users/interest/get_user_interest")
    suspend fun getUpdateInterests(
        @Query("userId") userId: String
    ): Response<UpdateInterestResponse>

    @GET("users/interest/check")
    suspend fun checkIfUserHasInterests(
        @Query("userId") userId: String
    ): Response<BaseApiResponse>

    @POST("users/interest/save")
    suspend fun saveUserInterest(
        @Body request: AddUserInterestRequest
    ): Response<AddInterestResponse>

    @POST("users/interest/update")
    suspend fun updateUserInterest(
        @Body request: AddUserInterestRequest
    ): Response<AddInterestResponse>

    @POST("users/follow")
    suspend fun followUser(
        @Body request: FollowUserRequest
    ): Response<FollowUserResponse>

    @POST("users/unfollow")
    suspend fun unfollowUser(
        @Body request: FollowUserRequest
    ): Response<UnFollowUserResponse>

    @POST("users/is_following")
    suspend fun isFollowingUser(
        @Body request: FollowUserRequest
    ): Response<UnFollowUserResponse>

    @GET("users/follow/list")
    suspend fun getFollowList(
        @Query("sourceId") sourceId: String,
        @Query("type") type: String
    ): Response<ListFollowResponse>

    @GET("post-image")
    suspend fun getNewsFeed(
        @Query("pageCount") pageCount: Int,
        @Query("pageNumber") pageNumber: Int,
        @Query("sorting") field: String,
    ): Response<PostResponse>

    @GET("post-image/detail")
    suspend fun getUserProfileNewsFeed(
        @Query("userId") userId: String,
        @Query("pageCount") pageCount: Int,
        @Query("pageNumber") pageNumber: Int
    ): Response<ImageUserProfilePost>

    @GET("post-image/post-detail")
    suspend fun getDetailPost(
        @Query("postId") postId: String,
    ): Response<ImageUserProfilePost>

    @POST("post-image")
    suspend fun addNewsImagePost(@Body request: PostNewsFeedRequest): Response<AddPostImageResponse>

    @POST("post-image/saved")
    suspend fun addSavedPost(@Body request: SavedPostRequest): Response<SavedPostResponse>

    @POST("post-image/saved/exist")
    suspend fun checkIfSavedPostExist(@Body request: SavedPostRequest): Response<CheckExistSavedPostResponse>

    @GET("post-image/saved")
    suspend fun getAllSavedPost(
        @Query("userId") userId: String,
        @Query("pageCount") pageCount: Int,
        @Query("pageNumber") pageNumber: Int
    ): Response<GetAllSavedPostResponse>

    @DELETE("post-image/delete")
    suspend fun deleteImagePost(@Query("postId") postId: String): Response<BaseApiResponse>

    @POST("notification/add")
    suspend fun addNotification(@Body request: AddNotificationRequest): Response<NotificationResponse>

    @DELETE("notification/remove")
    suspend fun removeNotification(@Query("id") id: Long): Response<BaseApiResponse>

    @GET("notification")
    suspend fun getAllNotification(
        @Query("ownerId") ownerId: String,
        @Query("pageCount") pageCount: Int,
        @Query("pageNumber") pageNumber: Int
    ): Response<GetNotificationResponse>

    @POST("group/create")
    suspend fun createGroup(@Body request: CreateGroupRequest): Response<CreateGroupResponse>

    @POST("group/update")
    suspend fun updateGroup(
        @Query("groupId") groupId: Long,
        @Body request: UpdateGroupRequest
    ): Response<CreateGroupResponse>

    @POST("group/invite")
    suspend fun addGroupInvitation(@Body request: CreateGroupInvitationRequest): Response<CreateInvitationResponse>

    @POST("group/request")
    suspend fun addGroupJoinRequest(@Body request: CreateGroupInvitationRequest): Response<CreateInvitationResponse>

    @GET("group/user")
    suspend fun getGroupByUserId(
        @Query("userId") userId: String, @Query("pageCount") pageCount: Int,
        @Query("pageNumber") pageNumber: Int
    ): Response<GetGroupResponse>

    @GET("group/owner")
    suspend fun getGroupByOwnerId(
        @Query("userId") userId: String, @Query("pageCount") pageCount: Int,
        @Query("pageNumber") pageNumber: Int
    ): Response<GetYourOwnGroupResponse>

    @GET("group/check/join")
    suspend fun checkIfJoinedGroup(
        @Query("userId") userId: String, @Query("groupId") groupId: Long
    ): Response<GetGroupByGroupIdAndMemberIdResponse>

    @GET("group/check/request")
    suspend fun checkIfRequestToJoinGroup(
        @Query("userId") userId: String, @Query("groupId") groupId: Long
    ): Response<CheckIfRequestToJoinGroupResponse>

    @GET("group")
    suspend fun getGroupById(
        @Query("groupId") groupId: Long
    ): Response<CreateGroupResponse>

    @GET("group/member")
    suspend fun getAllGroupMemberByGroupId(
        @Query("groupId") groupId: Long
    ): Response<GetGroupResponse>

    @GET("group/post")
    suspend fun getAllGroupPost(
        @Query("groupId") groupId: Long, @Query("pageCount") pageCount: Int,
        @Query("pageNumber") pageNumber: Int
    ): Response<GetAllPostByGroup>

    @GET("group/newsfeed")
    suspend fun getAllGroupNewsFeed(
        @Query("pageCount") pageCount: Int,
        @Query("pageNumber") pageNumber: Int
    ): Response<GetAllPostByGroup>

    @GET("group/all")
    suspend fun getAllGroups(
        @Query("userId") userId: String,
        @Query("pageCount") pageCount: Int,
        @Query("pageNumber") pageNumber: Int
    ): Response<GetAllGroupResponse>

    @GET("group/search")
    suspend fun searchGroup(
        @Query("groupName") groupName: String,
        @Query("pageCount") pageCount: Int,
        @Query("pageNumber") pageNumber: Int
    ): Response<GetAllGroupResponse>

    @POST("group/add/post")
    suspend fun addGroupPost(@Body request: CreateGroupPostRequest): Response<AddPostByGroupResponse>

    @POST("group/add/member")
    suspend fun addMemberToGroup(@Body request: JoinGroupRequest): Response<GetGroupByGroupIdAndMemberIdResponse>

    @POST("group/add/member/admin")
    suspend fun addAdminMemberToGroup(@Body request: JoinGroupRequest): Response<GetGroupByGroupIdAndMemberIdResponse>

    @DELETE("group/delete/group")
    suspend fun deleteGroup(@Query("groupId") groupId: Long): Response<BaseApiResponse>

    @HTTP(method = "DELETE", path = "group/delete/invitation", hasBody = true)
    suspend fun cancelInvitation(@Body request: JoinGroupRequest): Response<BaseApiResponse>

    @GET("group/search/post")
    suspend fun searchPostInGroup(
        @Query("groupId") groupId: Long,
        @Query("keyword") keyword: String
    ): Response<SearchPostInGroupResponse>

    @GET("group/get/request")
    suspend fun getAllGroupRequest(
        @Query("groupId") groupId: Long
    ): Response<GetGroupRequestResponse>

    @DELETE("group/remove/user")
    suspend fun removeUserFromGroup(
        @Query("userId") userId: String,
        @Query("groupId") groupId: Long
    ): Response<BaseApiResponse>

    @DELETE("group/remove/request")
    suspend fun removeGroupRequest(
        @Query("userId") userId: String,
        @Query("groupId") groupId: Long
    ): Response<BaseApiResponse>

    @GET("city/all/sorted")
    suspend fun getAllCities(): Response<GetLocationResponse>

    @GET("city/search")
    suspend fun searchForCity(@Query("keyword") keyword: String): Response<GetLocationResponse>

    @GET("story")
    suspend fun getAllStoryFolder(): StoryFolderResponse

    @POST("story")
    suspend fun addStoryFolder(@Body body: AddStoryFolderRequest): Response<AddStoryFolderResponse>

    @DELETE("story/delete/{id}")
    suspend fun deleteStoryFolder(@Path("id") id: Int): Response<BaseApiResponse>

}