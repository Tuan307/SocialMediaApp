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
}