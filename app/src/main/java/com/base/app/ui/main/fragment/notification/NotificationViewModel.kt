package com.base.app.ui.main.fragment.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.user.BaseApiResponse
import com.base.app.data.models.group.request.JoinGroupRequest
import com.base.app.data.models.response.GetNotificationResponse
import com.base.app.data.repositories.group.GroupRepository
import com.base.app.data.repositories.notification.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository,
    private val groupRepository: GroupRepository
) : BaseViewModel() {

    private var _getAllNotificationResponse: MutableLiveData<GetNotificationResponse> =
        MutableLiveData()
    val getAllNotificationResponse: LiveData<GetNotificationResponse>
        get() = _getAllNotificationResponse

    private var _getMoreNotificationResponse: MutableLiveData<GetNotificationResponse> =
        MutableLiveData()
    val getMoreNotificationResponse: LiveData<GetNotificationResponse>
        get() = _getMoreNotificationResponse

    private var _removeNotificationResponse: MutableLiveData<BaseApiResponse> =
        MutableLiveData()
    val removeNotificationResponse: LiveData<BaseApiResponse>
        get() = _removeNotificationResponse

    private var _removeNotificationErrorResponse: MutableLiveData<String> =
        MutableLiveData()
    val removeNotificationErrorResponse: LiveData<String>
        get() = _removeNotificationErrorResponse

    fun getAllNotification(pageCount: Int, pageNumber: Int) {
        viewModelScope.launch(handler) {
            val result =
                repository.getAllNotification(firebaseUser?.uid.toString(), pageCount, pageNumber)
            if (pageNumber == 1) {
                _getAllNotificationResponse.value = result
            } else {
                _getMoreNotificationResponse.value = result
            }
        }
    }

    private fun removeNotification(id: Long) {
        viewModelScope.launch(handler) {
            val result = repository.removeNotification(id)
            _removeNotificationResponse.value = result
        }
    }

    fun cancelJoinInvitation(groupId: Long, id: Long) {
        viewModelScope.launch(handler) {
            val result = groupRepository.cancelInvitation(
                JoinGroupRequest(
                    firebaseUser?.uid.toString(),
                    groupId
                )
            )
            if (result.status.message == "Successfully") {
                removeNotification(id)
            } else {
                _removeNotificationErrorResponse.value = result.status.message
            }
        }
    }

    fun joinGroup(groupId: Long, id: Long) {
        viewModelScope.launch(handler) {
            val result =
                groupRepository.addMemberToGroup(
                    JoinGroupRequest(
                        firebaseUser?.uid.toString(),
                        groupId
                    )
                )
            if (result.status?.message == "Successfully") {
                removeNotification(id)
            } else {
                _removeNotificationErrorResponse.value = result.status?.message
            }
        }
    }

}