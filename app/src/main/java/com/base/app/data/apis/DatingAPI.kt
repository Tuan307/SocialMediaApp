package com.base.app.data.apis

import com.base.app.data.models.dating_app.BaseApiResponse
import com.base.app.data.models.dating_app.SearchUserResponse
import com.base.app.data.models.dating_app.UserProfileResponseResult
import com.base.app.data.models.group.request.CreateGroupInvitationRequest
import com.base.app.data.models.group.request.CreateGroupPostRequest
import com.base.app.data.models.group.request.CreateGroupRequest
import com.base.app.data.models.group.request.JoinGroupRequest
import com.base.app.data.models.group.response.AddPostByGroupResponse
import com.base.app.data.models.group.response.CreateGroupResponse
import com.base.app.data.models.group.response.CreateInvitationResponse
import com.base.app.data.models.group.response.GetAllGroupResponse
import com.base.app.data.models.group.response.GetAllPostByGroup
import com.base.app.data.models.group.response.GetGroupByGroupIdAndMemberIdResponse
import com.base.app.data.models.group.response.GetGroupResponse
import com.base.app.data.models.group.response.GetYourOwnGroupResponse
import com.base.app.data.models.group.response.SearchPostInGroupResponse
import com.base.app.data.models.request.AddNotificationRequest
import com.base.app.data.models.request.PostNewsFeedRequest
import com.base.app.data.models.request.RegisterRequest
import com.base.app.data.models.request.SavedPostRequest
import com.base.app.data.models.response.GetNotificationResponse
import com.base.app.data.models.response.NotificationResponse
import com.base.app.data.models.response.post.AddPostImageResponse
import com.base.app.data.models.response.post.CheckExistSavedPostResponse
import com.base.app.data.models.response.post.GetAllSavedPostResponse
import com.base.app.data.models.response.post.ImageUserProfilePost
import com.base.app.data.models.response.post.PostResponse
import com.base.app.data.models.response.post.SavedPostResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author tuanpham
 * @since 9/18/2023
 */
interface DatingAPI {

    @GET("users/{id}")
    suspend fun getUserProfile(@Path("id") id: String): Response<UserProfileResponseResult>

    @POST("users/register")
    suspend fun registerUser(@Body user: RegisterRequest): Response<UserProfileResponseResult>

    @GET("users/search")
    suspend fun searchUsers(
        @Query("keyword") name: String, @Query("pageCount") pageCount: Int,
        @Query("pageNumber") pageNumber: Int
    ): Response<SearchUserResponse>

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

    @POST("group/add/post")
    suspend fun addGroupPost(@Body request: CreateGroupPostRequest): Response<AddPostByGroupResponse>

    @POST("group/add/member")
    suspend fun addMemberToGroup(@Body request: JoinGroupRequest): Response<GetGroupByGroupIdAndMemberIdResponse>

    @DELETE("group/delete/group")
    suspend fun deleteGroup(@Query("groupId") groupId: Long): Response<BaseApiResponse>

    @HTTP(method = "DELETE",path="group/delete/invitation", hasBody = true)
    suspend fun cancelInvitation(@Body request: JoinGroupRequest): Response<BaseApiResponse>

    @GET("group/search/post")
    suspend fun searchPostInGroup(
        @Query("groupId") groupId: Long,
        @Query("keyword") keyword: String
    ): Response<SearchPostInGroupResponse>
}