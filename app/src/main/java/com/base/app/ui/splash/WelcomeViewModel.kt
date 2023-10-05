package com.base.app.ui.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.apis.DatingAPI
import com.base.app.data.models.dating_app.DatingUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val api: DatingAPI
) : BaseViewModel() {

    var checkUserResponse = MutableLiveData<Boolean>()
    fun checkUser() {
        viewModelScope.launch {
            if (firebaseUser != null) {
                checkUserResponse.postValue(true)
            } else {
                checkUserResponse.postValue(false)
            }
        }
    }

    private var _getUserProfile: MutableLiveData<DatingUser> = MutableLiveData()
    val getUserProfileResponse: LiveData<DatingUser>
        get() = _getUserProfile

    fun getUserProfile() {
        Log.d("CheckHere", firebaseUser?.uid.toString())
        viewModelScope.launch(Dispatchers.IO) {
            val result = api.getUserProfile("1cCHibSQmjQuc65S3VhIfKJ16Nmu2")
            if (result.status.code == 200.toLong()) {
                _getUserProfile.postValue(result.data!!)
            } else {
                Log.d("CheckErr", "Here ${result.status.message}")
            }
        }
    }
}