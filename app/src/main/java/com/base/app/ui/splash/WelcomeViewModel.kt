package com.base.app.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class WelcomeViewModel : BaseViewModel() {

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
}