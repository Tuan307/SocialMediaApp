package com.base.app.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.common.IMAGE_URL
import com.base.app.data.models.user.UserProfileResponseResult
import com.base.app.data.models.request.RegisterRequest
import com.base.app.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: UserRepository
) : BaseViewModel() {

    private var _errorResponse: MutableLiveData<Boolean> = MutableLiveData()
    val errorResponse: LiveData<Boolean>
        get() = _errorResponse

    private var _saveToDBResponse: MutableLiveData<RegisterRequest> = MutableLiveData()
    val saveToDBResponse: LiveData<RegisterRequest>
        get() = _saveToDBResponse

    private var _registerUserToDBResponse: MutableLiveData<UserProfileResponseResult> =
        MutableLiveData()
    val registerUserToDBResponse: LiveData<UserProfileResponseResult>
        get() = _registerUserToDBResponse

    fun register(userName: String, fullName: String, email: String, password: String) {
        showLoading(true)
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    _errorResponse.postValue(true)
                    val uid = it.result.user?.uid.toString()
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["username"] = userName
                    hashMap["fullname"] = fullName
                    hashMap["bio"] = ""
                    hashMap["imageurl"] = IMAGE_URL
                    hashMap["id"] = uid
                    _saveToDBResponse.postValue(
                        RegisterRequest(
                            uid,
                            userName,
                            fullName,
                            IMAGE_URL,
                            "",
                            email
                        )
                    )
                    registerJobFinish()
                    databaseReference.child("Users").child(uid).updateChildren(hashMap)
                } else {
                    _errorResponse.postValue(false)
                    registerJobFinish()
                }
            }
        }
    }

    fun registerUser(user: RegisterRequest) {
        viewModelScope.launch(handler) {
            val result = repository.registerUser(user)
            _registerUserToDBResponse.value = result
        }
    }
}