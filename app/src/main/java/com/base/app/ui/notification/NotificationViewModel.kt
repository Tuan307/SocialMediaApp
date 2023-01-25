//package com.base.app.ui.notification
//
//import android.annotation.SuppressLint
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.base.app.base.viewmodel.BaseViewModel
//import com.base.app.data.models.response.NumberUnreadNotificationResponse
//import com.base.app.data.models.response.notification.NotificationResponse
//import com.base.app.data.repositories.NotificationRepository
//import com.base.app.common.DEFAULT_PAGE
//import com.base.app.data.models.response.Message
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@SuppressLint("CheckResult")
//@HiltViewModel
//class NotificationViewModel @Inject constructor(
//    private val repository: NotificationRepository
//) : BaseViewModel() {
//
////    private val notificationResponse = MutableLiveData<NotificationResponse>()
////    val getNotificationResponse = notificationResponse as LiveData<NotificationResponse>
////    private val notificationLoadMore = MutableLiveData<NotificationResponse>()
////    val getNotificationLoadMore = notificationLoadMore as LiveData<NotificationResponse>
////    fun getListNotification(page: Int, unRead: String?, type: String?) {
////        showLoading(true)
//////        parentJob = viewModelScope.launch(handler) {
//////            if (page == DEFAULT_PAGE) {
//////                val result =
//////                    repository.getListNotification(page = page, unRead = unRead, type = type)
//////                notificationResponse.postValue(result)
//////            } else {
//////                val result =
//////                    repository.getListNotification(page = page, unRead = unRead, type = type)
//////                notificationLoadMore.postValue(result)
//////            }
////   //     }
////        registerJobFinish()
////    }
////
////    private val numberUnreadNotification = MutableLiveData<NumberUnreadNotificationResponse>()
////    val getNumberUnreadNotification =
////        numberUnreadNotification as LiveData<NumberUnreadNotificationResponse>
////
////    fun getNumberUnreadNotification() {
//////        parentJob = viewModelScope.launch(handler) {
//////            val result =
//////                repository.getNumberUnreadNotification()
//////            numberUnreadNotification.postValue(result)
//////        }
////        registerJobFinish()
////    }
////
////    private val deleteNotification = MutableLiveData<Message>()
////    val getDeleteNotification = deleteNotification as LiveData<Message>
////    fun deleteNotification(id: String?) {
//////        parentJob = viewModelScope.launch(handler) {
//////            val result =
//////                repository.deleteNotification(id)
//////            deleteNotification.postValue(result)
//////        }
////        registerJobFinish()
////    }
////
////    private val readNotification = MutableLiveData<Message>()
////    val getReadNotification = readNotification as LiveData<Message>
////
////    fun readNotification(id: String?) {
//////        parentJob = viewModelScope.launch(handler) {
//////            val result =
//////                repository.readNotification(id)
//////            readNotification.postValue(result)
//////        }
////        registerJobFinish()
////
////    }
//
//}