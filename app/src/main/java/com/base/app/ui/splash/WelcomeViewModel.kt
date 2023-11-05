package com.base.app.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.data.models.interest.InterestModel
import com.base.app.data.repositories.UserRepository
import com.base.app.data.repositories.profile.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val repository: UserProfileRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {
    private var _userResponse: MutableLiveData<DatingUser?> = MutableLiveData()
    val userResponse: LiveData<DatingUser?>
        get() = _userResponse

    private var _interestResponse: MutableLiveData<List<InterestModel>> = MutableLiveData()
    val interestResponse: LiveData<List<InterestModel>>
        get() = _interestResponse

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

    fun getAllInterests() {
        viewModelScope.launch {
            val result = userRepository.getAllInterests()
            if (result.data != null) {
                _interestResponse.value = result.data.orEmpty()
            }
        }
    }
}