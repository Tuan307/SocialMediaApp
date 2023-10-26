package com.base.app.ui.group.detail_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.dating_app.BaseApiResponse
import com.base.app.data.models.group.request.JoinGroupRequest
import com.base.app.data.models.group.response.GetGroupByGroupIdAndMemberIdResponse
import com.base.app.data.models.group.response.GetGroupRequestResponse
import com.base.app.data.repositories.group.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/24/2023
 */
@HiltViewModel
class GroupAllRequestViewModel @Inject constructor(
    private val repository: GroupRepository
) : BaseViewModel() {

    private var _requestResponse: MutableLiveData<GetGroupRequestResponse> = MutableLiveData()
    val requestResponse: LiveData<GetGroupRequestResponse>
        get() = _requestResponse
    private var _agreeRequestResponse: MutableLiveData<GetGroupByGroupIdAndMemberIdResponse> =
        MutableLiveData()
    val agreeRequestResponse: LiveData<GetGroupByGroupIdAndMemberIdResponse>
        get() = _agreeRequestResponse
    private var _removeRequestGroupsResponse: MutableLiveData<BaseApiResponse> =
        MutableLiveData()
    val removeRequestGroupsResponse: LiveData<BaseApiResponse>
        get() = _removeRequestGroupsResponse

    fun getAllGroupRequest(groupId: Long) {
        viewModelScope.launch {
            val result = repository.getAllGroupRequest(groupId)
            _requestResponse.value = result
        }
    }

    fun joinGroup(groupId: Long, userId: String) {
        viewModelScope.launch {
            val result =
                repository.addMemberToGroup(
                    JoinGroupRequest(
                        userId,
                        groupId
                    )
                )
            _agreeRequestResponse.value = result
        }
    }

    fun removeGroupRequest(groupId: Long, userId: String) {
        viewModelScope.launch {
            val result = repository.removeGroupRequest(userId, groupId)
            _removeRequestGroupsResponse.value = result
        }
    }
}