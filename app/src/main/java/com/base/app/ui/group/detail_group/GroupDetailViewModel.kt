package com.base.app.ui.group.detail_group

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.R
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.dating_app.BaseApiResponse
import com.base.app.data.models.group.request.CreateGroupInvitationRequest
import com.base.app.data.models.group.request.JoinGroupRequest
import com.base.app.data.models.group.response.CreateInvitationResponse
import com.base.app.data.models.group.response.GetGroupByGroupIdAndMemberIdResponse
import com.base.app.data.models.mToken
import com.base.app.data.models.response.post.ImagesList
import com.base.app.data.repositories.group.GroupRepository
import com.base.app.ui.group.detail_group.viewdata.DetailGroupInformationViewData
import com.base.app.ui.group.detail_group.viewdata.DetailGroupPostViewData
import com.base.app.ui.group.detail_group.viewdata.DetailGroupViewData
import com.base.app.ui.group.detail_group.viewdata.SearchGroupViewData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/13/2023
 */
@HiltViewModel
class GroupDetailViewModel @Inject constructor(
    private val repository: GroupRepository
) : BaseViewModel() {
    var isJoined = false
    var isRequested = false
    private var prettyTime = PrettyTime(Locale.getDefault())
    var groupPrivacy = ""
    private var _listInformationResponse: MutableLiveData<DetailGroupInformationViewData> =
        MutableLiveData()
    val listInformationResponse: LiveData<DetailGroupInformationViewData>
        get() = _listInformationResponse

    private var _listInformationResponse1: MutableLiveData<DetailGroupViewData> =
        MutableLiveData()
    val listInformationResponse1: LiveData<DetailGroupViewData>
        get() = _listInformationResponse1

    private var _listGroupPostResponse: MutableLiveData<List<DetailGroupViewData>> =
        MutableLiveData()
    val listGroupPostResponse: LiveData<List<DetailGroupViewData>>
        get() = _listGroupPostResponse

    private var _checkIfJoinedGroupResponse: MutableLiveData<Boolean> =
        MutableLiveData()
    val checkIfJoinedGroupResponse: LiveData<Boolean>
        get() = _checkIfJoinedGroupResponse

    private var _removeUserFromGroupResponse: MutableLiveData<BaseApiResponse> =
        MutableLiveData()
    val removeUserFromGroupResponse: LiveData<BaseApiResponse>
        get() = _removeUserFromGroupResponse

    private var _joinGroupsResponse: MutableLiveData<GetGroupByGroupIdAndMemberIdResponse> =
        MutableLiveData()
    val joinGroupsResponse: LiveData<GetGroupByGroupIdAndMemberIdResponse>
        get() = _joinGroupsResponse

    private var _requestJoinGroupsResponse: MutableLiveData<CreateInvitationResponse> =
        MutableLiveData()
    val requestJoinGroupsResponse: LiveData<CreateInvitationResponse>
        get() = _requestJoinGroupsResponse

    private var _removeRequestGroupsResponse: MutableLiveData<BaseApiResponse> =
        MutableLiveData()
    val removeRequestGroupsResponse: LiveData<BaseApiResponse>
        get() = _removeRequestGroupsResponse

    fun joinGroup(groupId: Long) {
        viewModelScope.launch(handler) {
            val result =
                repository.addMemberToGroup(JoinGroupRequest(firebaseUser?.uid.toString(), groupId))
            _joinGroupsResponse.value = result
        }
    }

    fun requestJoinGroup(request: CreateGroupInvitationRequest) {
        viewModelScope.launch(handler) {
            val result = repository.createGroupJoinRequest(request)
            _requestJoinGroupsResponse.value = result
        }
    }

    fun removeGroupRequest(groupId: Long) {
        viewModelScope.launch(handler) {
            val result = repository.removeGroupRequest(firebaseUser?.uid.toString(), groupId)
            _removeRequestGroupsResponse.value = result
        }
    }

    fun checkIfJoinedGroup(groupId: Long) {
        viewModelScope.launch(handler) {
            val result = repository.checkIfJoinedGroup(firebaseUser?.uid.toString(), groupId)
            // _checkIfJoinedGroupResponse.value = result
        }
    }

    fun checkIfRequestToJoinGroup(groupId: Long) {
        viewModelScope.launch(handler) {
            val result = repository.checkIfRequestToJoinGroup(firebaseUser?.uid.toString(), groupId)
            when (result.data) {
                2 -> {
                    isJoined = true
                    isRequested = false
                }
                0 -> {
                    isJoined = false
                    isRequested = false
                }
                1 -> {
                    isRequested = true
                    isJoined = false
                }
            }
            _checkIfJoinedGroupResponse.value = true
        }
    }

    fun removeUserFromGroup(groupId: Long) {
        viewModelScope.launch(handler) {
            val result = repository.removeUserFromGroup(firebaseUser?.uid.toString(), groupId)
            _removeUserFromGroupResponse.value = result
        }
    }

    fun getGroupInformation(groupId: Long, isJoined: Boolean, hasRequested: Boolean) {
        viewModelScope.launch(handler) {
            val result = repository.getGroupById(groupId)
            val groupData = result.data
            val detailInformation = DetailGroupInformationViewData(
                id = groupData?.id.toString(),
                imageUrl = groupData?.groupImageUrl.toString(),
                userImageUrl = groupData?.groupImageUrl.toString(),
                groupName = groupData?.groupName.toString(),
                groupPrivacy = groupData?.groupPrivacy.toString(),
                groupMemberNumber = "",
                hasJoined = isJoined,
                groupOwnerId = groupData?.groupOwner?.userId.toString(),
                hasRequested = hasRequested
            )
            groupPrivacy = groupData?.groupPrivacy.toString()
            _listInformationResponse.value = detailInformation
        }
    }

    fun getAllGroupMemberInformation(groupId: Long, data: DetailGroupInformationViewData) {
        viewModelScope.launch(handler) {
            val result = repository.getAllGroupMemberByGroupId(groupId)
            val detailInformation = DetailGroupInformationViewData(
                id = data.id,
                imageUrl = data.imageUrl,
                userImageUrl = data.imageUrl,
                groupName = data.groupName,
                groupPrivacy = data.groupPrivacy,
                groupMemberNumber = "${result.data?.size} thành viên",
                hasJoined = data.hasJoined,
                groupOwnerId = data.groupOwnerId,
                hasRequested = data.hasRequested
            )
            _listInformationResponse1.value = detailInformation
        }
    }

    fun getGroupPost(groupId: Long, pageCount: Int, pageNumber: Int) {
        viewModelScope.launch(handler) {
            val result = repository.getAllGroupPost(groupId, pageCount, pageNumber)
            _listGroupPostResponse.value = result.data?.content?.map { data ->
                DetailGroupPostViewData(
                    id = data.groupPostId,
                    description = data.description,
                    imagesList = data.groupPostContentItemList.map { it1 ->
                        ImagesList(
                            id = it1.id,
                            imageUrl = it1.imageUrl
                        )
                    },
                    createdAt = prettyTime.format(Date(data.createdAt.toLong())),
                    checkInAddress = data.checkInAddress,
                    checkInLongitude = data.checkInLongitude,
                    checkInLatitude = data.checkInLatitude,
                    type = data.type,
                    videoUrl = data.videoUrl,
                    question = data.question,
                    user = data.groupPostUserId
                )
            }
        }
    }

    fun isLikeGroupPost(postId: String, image: ImageView) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Group_Likes").child(postId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val uid = firebaseUser?.uid
                        if (uid != null) {
                            if (snapshot.child(uid).exists()) {
                                image.tag = "liked"
                                image.setImageResource(R.drawable.ic_like)
                            } else {
                                image.setImageResource(R.drawable.ic_heart_border)
                                image.tag = "like"
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }

    }

    fun isSavedPost(postId: String, image: ImageView) {
        viewModelScope.launch(handler) {
//            val result = newsFeedRepository.checkIfSavedPostExist(
//                SavedPostRequest(
//                    firebaseUser?.uid.toString(),
//                    postId
//                )
//            )
//            if (result.data != null) {
//                image.tag = "saved"
//                image.setImageResource(R.drawable.ic_bookmark_boder_black)
//            } else {
//                image.setImageResource(R.drawable.ic_bookmark_border)
//                image.tag = "save"
//            }
        }
    }

    fun getGroupPostLikes(text: TextView, postId: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Group_Likes").child(postId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val count = snapshot.childrenCount
                        text.text = "$count likes"
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun countGroupPostComments(text: TextView, postId: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Group_Comments").child(postId)
                .addValueEventListener(object : ValueEventListener {
                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        text.text = "View all ${snapshot.childrenCount} comments"
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun likeGroupPost(postId: String, status: String, publisherId: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            if (status == "like") {
                databaseReference.child("Group_Likes").child(postId)
                    .child(firebaseUser?.uid ?: "none")
                    .setValue(true)
                if (publisherId != firebaseUser?.uid.toString()) {
                    //addNotifications(postId, publisherId)
                }
            } else {
                databaseReference.child("Group_Likes").child(postId)
                    .child(firebaseUser?.uid ?: "none")
                    .removeValue()
            }
        }
    }

    var tokenResponse = MutableLiveData<String>()
    fun getReceiverToken(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Tokens").child(id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val token = snapshot.getValue(mToken::class.java)
                        if (token != null) {
                            tokenResponse.postValue(token.token)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    private var _searchPostInGroupResponse: MutableLiveData<List<SearchGroupViewData>> =
        MutableLiveData()
    val searchPostInGroupResponse: LiveData<List<SearchGroupViewData>>
        get() = _searchPostInGroupResponse

    fun searchPostInGroup(groupId: Long, keyword: String) {
        viewModelScope.launch(handler) {
            val result = repository.searchPostInGroup(groupId, keyword)
            _searchPostInGroupResponse.value = result.data?.map { data ->
                SearchGroupViewData(
                    groupPostId = data.groupPostId,
                    description = data.description,
                    groupPostContentItemList = data.groupPostContentItemList.map {
                        ImagesList(it.id, it.imageUrl)
                    },
                    groupPostUser = data.groupPostUserId,
                    groupModel = data.groupPostModelId,
                    createdAt = data.createdAt,
                    checkInAddress = data.checkInAddress,
                    checkInLatitude = data.checkInLatitude,
                    checkInLongitude = data.checkInLongitude,
                    type = data.type,
                    videoUrl = data.videoUrl,
                    question = data.question
                )
            }
        }
    }

    private var _deletePostResponse = MutableLiveData<String>()
    val deletePostResponse: LiveData<String>
        get() = _deletePostResponse

    fun deleteGroupPost(postId: String) {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
//            val result = newsFeedRepository.deleteNewPost(postId)
//            _deletePostResponse.value = result.status.message
            registerJobFinish()
        }
    }
}