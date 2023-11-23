package com.base.app.ui.main.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.CustomApplication.Companion.dataManager
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.common.ERROR
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.data.models.request.AddNotificationRequest
import com.base.app.data.models.response.post.GetAllSavedPostResponse
import com.base.app.data.models.response.post.PostContent
import com.base.app.data.repositories.notification.NotificationRepository
import com.base.app.data.repositories.profile.UserProfileRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserProfileRepository,
    private val notificationRepository: NotificationRepository
) : BaseViewModel() {

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

    private var followerNumber = MutableLiveData<Long>()
    val getFollowerNumber = followerNumber as LiveData<Long>
    fun getFollower(id: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Follow").child(id).child("follower")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        followerNumber.postValue(snapshot.childrenCount)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        responseMessage.postValue(ERROR)
                    }
                })
        }
    }

    private var followingNumber = MutableLiveData<Long>()
    val getFollowingNumber = followingNumber as LiveData<Long>
    fun getFollowing(id: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Follow").child(id).child("following")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        followingNumber.postValue(snapshot.childrenCount)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        responseMessage.postValue(ERROR)
                    }
                })
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

    fun followUser(t: Boolean, id: String) {
        if (t) {
            databaseReference.child("Follow").child(firebaseUser?.uid.toString())
                .child("following")
                .child(id).setValue(true)
            databaseReference.child("Follow").child(id).child("follower")
                .child(firebaseUser?.uid.toString()).setValue(true)
        } else {
            databaseReference.child("Follow").child(firebaseUser?.uid.toString())
                .child("following")
                .child(id).removeValue()
            databaseReference.child("Follow").child(id).child("follower")
                .child(firebaseUser?.uid.toString()).removeValue()
        }
    }

    var followResponse = MutableLiveData<Boolean>()
    fun isFollowing(id: String) {
        if (id != "") {
            databaseReference.child("Follow").child(firebaseUser?.uid.toString())
                .child("following").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(id).exists()) {
                            followResponse.postValue(true)
                        } else {
                            followResponse.postValue(false)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun addNotifications(profileId: String) {
        val userName = userRemoteResponse.value?.userName ?: "Someone"
        viewModelScope.launch(handler) {
            notificationRepository.addNotification(
                AddNotificationRequest(
                    isPost = false,
                    isInvitation = false,
                    text = " started following you",
                    ownerId = profileId,
                    postId = "",
                    timeStamp = Calendar.getInstance().time.time.toString(),
                    userId = firebaseUser?.uid.toString()
                )
            )
        }

    }
}