package com.base.app.ui.edit_profile

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.data.models.dating_app.UserUpdateProfileResponse
import com.base.app.data.models.request.UpdateProfileRequest
import com.base.app.data.repositories.profile.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val repository: UserProfileRepository
) : BaseViewModel() {

    private var userInformation = MutableLiveData<DatingUser?>()
    val getUserInformation = userInformation as LiveData<DatingUser?>

    private var _updateProfileRemote = MutableLiveData<UserUpdateProfileResponse>()
    val updateProfileRemote: LiveData<UserUpdateProfileResponse>
        get() = _updateProfileRemote

    private var _updateProfileRemoteRequest = MutableLiveData<UpdateProfileRequest>()
    val updateProfileRemoteRequest: LiveData<UpdateProfileRequest>
        get() = _updateProfileRemoteRequest

    fun getInformation() {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val result = repository.getUserProfile(firebaseUser?.uid.toString())
            userInformation.value = result.data
            Handler(Looper.getMainLooper()).postDelayed({
                registerJobFinish()
            }, 1000)
        }
    }

    var uploadImageResponse = MutableLiveData<Boolean>()
    fun updateProfileRequest(
        userId: String,
        fullName: String,
        userName: String,
        bio: String,
        imageUri: Uri?,
        imagePath: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (imageUri != null && imagePath != null) {
                val ref = storageRef.child("uploads").child(
                    System.currentTimeMillis().toString() + "." +
                            imagePath
                )
                uploadTask = ref.putFile(imageUri)
                uploadTask!!.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    ref.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result.toString()
                        val request = UpdateProfileRequest(
                            userId = userId,
                            fullName = fullName,
                            userName = userName,
                            bio = bio,
                            imageUrl = downloadUri
                        )
                        _updateProfileRemoteRequest.postValue(request)
                    } else {
                        uploadImageResponse.postValue(false)
                    }
                }.addOnFailureListener {
                    uploadImageResponse.postValue(false)
                }
            } else {
                val request = UpdateProfileRequest(
                    userId = userId,
                    fullName = fullName,
                    userName = userName,
                    bio = bio,
                    imageUrl = ""
                )
                _updateProfileRemoteRequest.postValue(request)
            }
        }
    }

    fun updateProfile(request: UpdateProfileRequest) {
        showLoading(true)
        viewModelScope.launch {
            val result = repository.updateUserProfile(request)
            _updateProfileRemote.value = result
            Handler(Looper.getMainLooper()).postDelayed({
                registerJobFinish()
            }, 1000)
        }
    }

    fun updateProfileFireBase(
        fullName: String,
        userName: String,
        bio: String,
        imageUrl: String
    ) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            val hashMap = HashMap<String, Any>()
            hashMap["fullname"] = fullName
            hashMap["username"] = userName
            hashMap["bio"] = bio
            hashMap["imageurl"] = imageUrl
            firebaseUser?.let {
                databaseReference.child("Users").child(it.uid).updateChildren(hashMap)
                    .addOnCompleteListener { it1 ->
                        if (it1.isSuccessful) {
                            Log.d("CheckResult", "Successfully")
                        } else {
                            Log.d("CheckResult", "Error")
                        }
                    }
            }
        }
    }
}