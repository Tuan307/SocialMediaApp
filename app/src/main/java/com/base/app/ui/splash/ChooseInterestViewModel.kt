package com.base.app.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.interest.InterestModel
import com.base.app.data.models.interest.UpdateInterestResponse
import com.base.app.data.models.interest.request.AddUserInterestRequest
import com.base.app.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 11/29/2023
 */
@HiltViewModel
class ChooseInterestViewModel @Inject constructor(
    private val userRepository: UserRepository

) : BaseViewModel() {

    private var _interestResponse: MutableLiveData<List<InterestModel>> = MutableLiveData()
    val interestResponse: LiveData<List<InterestModel>>
        get() = _interestResponse

    private var _addInterestResponse: MutableLiveData<Boolean> = MutableLiveData()
    val addInterestResponse: LiveData<Boolean>
        get() = _addInterestResponse

    private var _updateAllInterest: MutableLiveData<UpdateInterestResponse> = MutableLiveData()
    val updateAllInterest: LiveData<UpdateInterestResponse>
        get() = _updateAllInterest

    fun getUpdateInterests() {
        viewModelScope.launch(handler) {
            val result = userRepository.getUpdateInterests(firebaseUser?.uid.toString())
            _updateAllInterest.value = result
        }
    }

    fun getAllInterests() {
        viewModelScope.launch(handler) {
            val result = userRepository.getAllInterests()
            if (result.data != null) {
                _interestResponse.value = result.data.orEmpty()
            }
        }
    }

    fun saveUserInterest(request: AddUserInterestRequest) {
        viewModelScope.launch(handler) {
            val result = userRepository.saveUserInterest(request)
            _addInterestResponse.value = !result.data.isNullOrEmpty()
        }
    }

    fun updateUserInterest(request: AddUserInterestRequest) {
        viewModelScope.launch(handler) {
            val result = userRepository.updateUserInterest(request)
            _addInterestResponse.value = !result.data.isNullOrEmpty()
        }
    }
}