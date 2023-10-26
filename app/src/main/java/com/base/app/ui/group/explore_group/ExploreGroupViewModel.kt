package com.base.app.ui.group.explore_group

import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.dating_app.BaseApiResponse
import com.base.app.data.models.group.request.CreateGroupInvitationRequest
import com.base.app.data.models.group.request.JoinGroupRequest
import com.base.app.data.models.group.response.CreateInvitationResponse
import com.base.app.data.models.group.response.GetGroupByGroupIdAndMemberIdResponse
import com.base.app.data.repositories.group.GroupRepository
import com.base.app.ui.group.explore_group.viewdata.GroupDataViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/19/2023
 */
@HiltViewModel
class ExploreGroupViewModel @Inject constructor(
    private val repository: GroupRepository
) : BaseViewModel() {

    private var _getAllGroupsResponse: MutableLiveData<List<GroupDataViewData>> = MutableLiveData()
    val getAllGroupsResponse: LiveData<List<GroupDataViewData>>
        get() = _getAllGroupsResponse

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

    fun getAllGroups(pageCount: Int, page: Int) {
        viewModelScope.launch {
            val result = repository.getAllGroups(firebaseUser?.uid.toString(), pageCount, page)
            if (page == 1) {
                _getAllGroupsResponse.value = result.data?.map { data ->
                    GroupDataViewData(
                        id = data.id ?: 0,
                        groupName = data.groupName.orEmpty(),
                        groupDescription = data.groupDescription.orEmpty(),
                        groupImageUrl = data.groupImageUrl.orEmpty(),
                        groupCreatedAt = data.groupCreatedAt.orEmpty(),
                        groupOwner = data.groupOwner,
                        groupPrivacy = data.groupPrivacy.orEmpty(),
                        hasJoined = false,
                    )
                }
            }
        }
    }

    fun removeGroupRequest(groupId: Long){
        viewModelScope.launch {
            val result = repository.removeGroupRequest(firebaseUser?.uid.toString(), groupId)
            _removeRequestGroupsResponse.value = result
        }
    }

    fun getAllGroupMemberInformation(groupId: Long, view: AppCompatTextView) {
        viewModelScope.launch {
            val result = repository.getAllGroupMemberByGroupId(groupId)
            view.text = "${result.data?.size} thành viên"
        }
    }

    fun joinGroup(groupId: Long) {
        viewModelScope.launch {
            val result =
                repository.addMemberToGroup(JoinGroupRequest(firebaseUser?.uid.toString(), groupId))
            _joinGroupsResponse.value = result
        }
    }

    fun requestJoinGroup(request: CreateGroupInvitationRequest) {
        viewModelScope.launch {
            val result = repository.createGroupJoinRequest(request)
            _requestJoinGroupsResponse.value = result
        }
    }
}