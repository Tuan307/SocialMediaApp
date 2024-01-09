package com.base.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.user.BaseApiResponse
import com.base.app.data.models.user.User
import com.base.app.data.repositories.UserRepository
import com.base.app.data.repositories.profile.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val userProfileRepository: UserProfileRepository,
) : BaseViewModel() {

    private var _userInterestResponse: MutableLiveData<BaseApiResponse> = MutableLiveData()
    val userInterestResponse: LiveData<BaseApiResponse>
        get() = _userInterestResponse
    private var userUID = ""
    private var loginResponse = MutableLiveData<Boolean>()
    val getLoginResponse = loginResponse as LiveData<Boolean>

    private var userRemoteResponse = MutableLiveData<User?>()
    val getUserRemoteResponse = userRemoteResponse as LiveData<User?>
    fun getRemoteUserInformation() {
        parentJob = viewModelScope.launch(handler) {
            val result = userProfileRepository.getUserProfile(userUID)
            if (result.data != null) {
                userRemoteResponse.postValue(result.data)
            } else {
                responseMessage.postValue(result.status.message)
            }
        }
    }

    fun login(email: String, password: String) {
        showLoading(true)
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    userUID = it.result.user?.uid.toString()
                    loginResponse.postValue(true)
                    registerJobFinish()
                } else {
                    loginResponse.postValue(false)
                    registerJobFinish()
                }
            }
        }
    }

    fun checkIfInterestExist() {
        viewModelScope.launch(handler) {
            val result = repository.checkIfUserHasInterests(userUID)
            _userInterestResponse.value = result
        }
    }

    private var sendResetPasswordResponse = MutableLiveData<Boolean>()
    val getSendResetPasswordResponse = sendResetPasswordResponse as LiveData<Boolean>
    fun forgetPassword(email: String) {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
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