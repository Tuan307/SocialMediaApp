package com.base.app.ui.main.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.CustomApplication.Companion.dataManager
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.data.models.request.AddNotificationRequest
import com.base.app.data.models.request.FollowUserRequest
import com.base.app.data.models.response.FollowUserResponse
import com.base.app.data.models.response.ListFollowResponse
import com.base.app.data.models.response.UnFollowUserResponse
import com.base.app.data.models.response.post.GetAllSavedPostResponse
import com.base.app.data.models.response.post.PostContent
import com.base.app.data.repositories.UserRepository
import com.base.app.data.repositories.notification.NotificationRepository
import com.base.app.data.repositories.profile.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserProfileRepository,
    private val userRepository: UserRepository,
    private val notificationRepository: NotificationRepository
) : BaseViewModel() {

    private var _followUserResponse: MutableLiveData<FollowUserResponse> = MutableLiveData()
    val followUserResponse: LiveData<FollowUserResponse>
        get() = _followUserResponse

    private var _unfollowUserResponse: MutableLiveData<UnFollowUserResponse> = MutableLiveData()
    val unfollowUserResponse: LiveData<UnFollowUserResponse>
        get() = _unfollowUserResponse

    private var _isFollowingUserResponse: MutableLiveData<UnFollowUserResponse> = MutableLiveData()
    val isFollowingUserResponse: LiveData<UnFollowUserResponse>
        get() = _isFollowingUserResponse

    private var _getFollowingResponse = MutableLiveData<ListFollowResponse>()
    val getFollowingResponse: LiveData<ListFollowResponse>
        get() = _getFollowingResponse

    private var _getFollowerResponse = MutableLiveData<ListFollowResponse>()
    val getFollowerResponse: LiveData<ListFollowResponse>
        get() = _getFollowerResponse

    private var key = MutableLiveData<String?>()
    val getKey = key as LiveData<String?>
    fun getKey(t: Boolean) {
        if (t) {
            if (dataManager.getString("id") != null) {
                key.postValue(dataManager.getString("id"))
            }
        } else {
            key.postValue(firebaseUser?.uid.toString())
        }
    }

    var statusId = MutableLiveData<Boolean>()
    fun setId(id: String) {
        if (id == firebaseUser?.uid.toString()) {
            statusId.postValue(true)
        } else {
            statusId.postValue(false)
        }
    }

    private var userRemoteResponse = MutableLiveData<DatingUser?>()
    val getUserRemoteResponse = userRemoteResponse as LiveData<DatingUser?>
    fun getRemoteUserInformation(id: String) {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val result = repository.getUserProfile(id)
            if (result.data != null) {
                userRemoteResponse.postValue(result.data)
            } else {
                responseMessage.postValue(result.status.message)
            }
            registerJobFinish()
        }
    }


    fun getFollow(sourceId: String, type: String) {
        viewModelScope.launch(handler) {
            val result = userRepository.getFollowList(sourceId, type)
            if (type == "follow") {
                _getFollowingResponse.value = result
            } else {
                _getFollowerResponse.value = result
            }
        }
    }

    private var profilePost = MutableLiveData<List<PostContent>>()
    val getProfilePost = profilePost as LiveData<List<PostContent>>
    private var moreProfilePost = MutableLiveData<List<PostContent>>()
    val getMoreProfilePost = moreProfilePost as LiveData<List<PostContent>>
    fun getProfilePost(id: String, pageCount: Int, pageNumber: Int) {
        viewModelScope.launch(handler) {
            val result = repository.getUserProfileImagePost(id, pageCount, pageNumber)
            if (pageNumber == 1) {
                profilePost.value = result.data.orEmpty()
            } else {
                moreProfilePost.value = result.data.orEmpty()
            }
        }
    }

    private var _getSavedPostResponse = MutableLiveData<GetAllSavedPostResponse>()
    val getSavedPostResponse: LiveData<GetAllSavedPostResponse>
        get() = _getSavedPostResponse
    private var _getMoreSavedPostResponse = MutableLiveData<GetAllSavedPostResponse>()
    val getMoreSavedPostResponse: LiveData<GetAllSavedPostResponse>
        get() = _getMoreSavedPostResponse

    fun getSavePost(userId: String, pageCount: Int, pageNumber: Int) {
        viewModelScope.launch(handler) {
            val result =
                repository.getAllSavedPost(userId, pageCount, pageNumber)
            if (pageNumber == 1) {
                _getSavedPostResponse.value = result
            } else {
                _getMoreSavedPostResponse.value = result
            }
        }
    }

    fun followUser(request: FollowUserRequest) {
        viewModelScope.launch(handler) {
            val result = userRepository.followUser(request)
            _followUserResponse.value = result
        }
    }

    fun unfollowUser(request: FollowUserRequest) {
        viewModelScope.launch(handler) {
            val result = userRepository.unfollowUser(request)
            _unfollowUserResponse.value = result
        }
    }

    fun isFollowing(request: FollowUserRequest) {
        viewModelScope.launch(handler) {
            val result = userRepository.isFollowingUser(request)
            _isFollowingUserResponse.value = result
        }
    }

    fun addNotifications(profileId: String,userName:String) {
        viewModelScope.launch(handler) {
            notificationRepository.addNotification(
                AddNotificationRequest(
                    isPost = false,
                    isInvitation = false,
                    text = "$userName started following you",
                    ownerId = profileId,
                    postId = "",
                    timeStamp = Calendar.getInstance().time.time.toString(),
                    userId = firebaseUser?.uid.toString()
                )
            )
        }

    }
}