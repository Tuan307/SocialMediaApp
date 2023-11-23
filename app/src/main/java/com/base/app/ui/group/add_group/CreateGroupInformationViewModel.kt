package com.base.app.ui.group.add_group

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.common.Event
import com.base.app.data.models.group.response.CreateGroupResponse
import com.base.app.data.models.group.request.CreateGroupRequest
import com.base.app.data.repositories.group.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/10/2023
 */
@HiltViewModel
class CreateGroupInformationViewModel @Inject constructor(
    private val repository: GroupRepository
) : BaseViewModel() {
    private var _uploadImageToStorage: MutableLiveData<Event<String>> = MutableLiveData()
    val uploadImageToStorage: LiveData<Event<String>>
        get() = _uploadImageToStorage

    private var _createGroupResponse: MutableLiveData<Event<CreateGroupResponse>> =
        MutableLiveData()
    val createGroupResponse: LiveData<Event<CreateGroupResponse>>
        get() = _createGroupResponse

    fun uploadImage(uri: Uri?, path: String?) {
        if (uri != null && path != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val ref = storageRef.child("new_posts")
                    .child(System.currentTimeMillis().toString() + "." + path)
                uploadTask = ref.putFile(uri)
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
                        _uploadImageToStorage.postValue(Event(downloadUri))
                    }
                }.addOnFailureListener {
                }
            }
        }
    }

    fun createGroup(request: CreateGroupRequest) {
        viewModelScope.launch(handler) {
            val result = repository.createGroup(request)
            _createGroupResponse.value = Event(result)
        }
    }
}