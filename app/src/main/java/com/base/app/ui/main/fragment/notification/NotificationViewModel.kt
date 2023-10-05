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

    fun getAllNotification() {
        viewModelScope.launch {
            val result = repository.getAllNotification(firebaseUser?.uid.toString(), 10, 1)
            _getAllNotificationResponse.value = result
        }
    }
}