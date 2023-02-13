package com.base.app.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    private var loginResponse = MutableLiveData<Boolean>()
    val getLoginResponse = loginResponse as LiveData<Boolean>
    fun login(email: String, password: String) {
        showLoading(true)
        Log.d("CheckLoading", isLoading.value.toString())
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    loginResponse.postValue(true)
                    registerJobFinish()
                } else {
                    loginResponse.postValue(false)
                }
            }
        }
    }

    private var sendResetPasswordResponse = MutableLiveData<Boolean>()
    val getSendResetPasswordResponse = sendResetPasswordResponse as LiveData<Boolean>
    fun forgetPassword(email: String) {
        showLoading(true)
        parentJob = viewModelScope.launch {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    sendResetPasswordResponse.postValue(true)
                    registerJobFinish()
                } else {
                    sendResetPasswordResponse.postValue(false)
                }
            }
        }
    }
}