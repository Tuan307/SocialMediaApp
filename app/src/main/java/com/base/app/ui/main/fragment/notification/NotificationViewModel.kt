package com.base.app.ui.main.fragment.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.response.GetNotificationResponse
import com.base.app.data.repositories.notification.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository
) : BaseViewModel() {

    private var _getAllNotificationResponse: MutableLiveData<GetNotificationResponse> =
        MutableLiveData()
    val getAllNotificationResponse: LiveData<GetNotificationResponse>
        get() = _getAllNotificationResponse
    private var _getMoreNotificationResponse: MutableLiveData<GetNotificationResponse> =
        MutableLiveData()
    val getMoreNotificationResponse: LiveData<GetNotificationResponse>
        get() = _getMoreNotificationResponse

    fun getAllNotification(pageCount: Int, pageNumber: Int) {
        viewModelScope.launch {
            val result =
                repository.getAllNotification(firebaseUser?.uid.toString(), pageCount, pageNumber)
            if (pageNumber == 1) {
                _getAllNotificationResponse.value = result
            } else {
                _getMoreNotificationResponse.value = result
            }
        }
    }

}