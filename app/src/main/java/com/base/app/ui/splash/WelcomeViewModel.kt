package com.base.app.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.data.repositories.profile.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val repository: UserProfileRepository
) : BaseViewModel() {
    private var _userResponse: MutableLiveData<DatingUser?> = MutableLiveData()
    val userResponse: LiveData<DatingUser?>
        get() = _userResponse
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

    fun getUserProfile() {
        viewModelScope.launch {
            val result = repository.getUserProfile(firebaseUser?.uid.toString())
            _userResponse.value = result.data
        }
    }
}