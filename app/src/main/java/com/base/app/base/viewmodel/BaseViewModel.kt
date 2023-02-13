package com.base.app.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.base.app.CustomApplication.Companion.dataManager
import com.base.app.base.network.BaseNetworkException
import com.base.app.base.network.NetworkErrorException
import com.base.app.common.Event
import com.base.app.common.FIREBASE_URL
import com.base.app.common.PPREF_PIN_STATUS
import com.base.app.common.STORAGE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {

    private val storage = Firebase.storage(STORAGE_URL)
    val storageRef = storage.reference
    var uploadTask: UploadTask? = null
    val databaseReference: DatabaseReference = Firebase.database(FIREBASE_URL).reference
    val auth = Firebase.auth
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseUser = firebaseAuth.currentUser
    val isLoadingProgress: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<Int> = MutableLiveData()
    val responseMessage: MutableLiveData<String?> = MutableLiveData()
    var baseNetworkException = MutableLiveData<Event<BaseNetworkException>>()
        protected set

    var networkException = MutableLiveData<Event<NetworkErrorException>>()
        protected set

    var isLoading = MutableLiveData<Boolean>()

    var onNavigateToPage = MutableLiveData<Event<Int>>()
        protected set

    var errorMessageResourceId = MutableLiveData<Event<Int>>()
        protected set

    var notifyMessageResourceId = MutableLiveData<Event<Int>>()
        protected set

    var isLoadingMore = MutableLiveData<Event<Boolean>>()
        protected set

    var parentJob: Job? = null
        protected set

    protected fun registerJobFinish() {
        parentJob?.invokeOnCompletion {
            showLoading(false)
        }
    }

    val handler = CoroutineExceptionHandler { _, exception ->
        parseErrorCallApi(exception)
    }

    protected fun showError(messageId: Int) {
        errorMessageResourceId.postValue(Event(messageId))
    }

    protected fun showNotify(messageId: Int) {
        notifyMessageResourceId.postValue(Event(messageId))
    }

    protected fun addBaseNetworkException(exception: BaseNetworkException) {
        baseNetworkException.postValue(Event(exception))
    }

    fun savePinStatus(value: Boolean) {
        dataManager.save(PPREF_PIN_STATUS, value)
    }

    fun getPinStatus(): Boolean {
        return dataManager.getBoolean(PPREF_PIN_STATUS)
    }

    protected fun addNetworkException(exception: NetworkErrorException) {
        networkException.postValue(Event(exception))
    }

    fun showLoading(isShow: Boolean) {
        isLoading.postValue(isShow)
    }

    protected fun showLoadingMore(isShow: Boolean) {
        isLoadingMore.postValue(Event(isShow))
    }

    protected fun navigateToPage(actionId: Int) {
        onNavigateToPage.postValue(Event(actionId))
    }

    protected open fun parseErrorCallApi(e: Throwable) {
        when (e) {
            is BaseNetworkException -> {
                baseNetworkException.postValue(Event(e))
            }
            is NetworkErrorException -> {
                networkException.postValue(Event(e))
            }
            else -> {
                val unknowException = BaseNetworkException()
                unknowException.mainMessage = e.message ?: "Something went wrong"
                baseNetworkException.postValue(Event(unknowException))
            }
        }
    }


    open fun fetchData() {

    }

}