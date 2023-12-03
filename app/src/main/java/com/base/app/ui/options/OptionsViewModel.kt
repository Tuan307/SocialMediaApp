package com.base.app.ui.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.common.ERROR
import com.base.app.data.models.User
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.data.models.response.ListFollowResponse
import com.base.app.data.repositories.UserRepository
import com.base.app.data.repositories.profile.UserProfileRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OptionsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val repository: UserProfileRepository,
) : BaseViewModel() {

    private var _getFollowingResponse = MutableLiveData<ListFollowResponse>()
    val getFollowingResponse: LiveData<ListFollowResponse>
        get() = _getFollowingResponse

    private var _getFollowerResponse = MutableLiveData<ListFollowResponse>()
    val getFollowerResponse: LiveData<ListFollowResponse>
        get() = _getFollowerResponse

    private var _userRemoteResponse = MutableLiveData<DatingUser?>()
    val userRemoteResponse = _userRemoteResponse as LiveData<DatingUser?>

    fun getRemoteUserInformation(id: String) {
        showLoading(true)
        viewModelScope.launch(handler) {
            val result = repository.getUserProfile(id)
            if (result.data != null) {
                _userRemoteResponse.value = result.data
            } else {
                responseMessage.postValue(result.status.message)
            }
            registerJobFinish()
        }
    }

    var logOutResponse = MutableLiveData<Boolean>()
    fun logOut() {
        viewModelScope.launch(handler) {
            auth.signOut()
            logOutResponse.postValue(true)
        }
    }
    fun getFollow(sourceId: String, type: String) {
        viewModelScope.launch(handler) {
            val result = userRepository.getFollowList(sourceId, type)
            if (type == "follow") {
                _getFollowingResponse.value = result
            } else {
                _getFollowerResponse.value = result
            }
        }
    }
}