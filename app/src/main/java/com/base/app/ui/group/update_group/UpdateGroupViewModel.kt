package com.base.app.ui.group.update_group

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.common.Event
import com.base.app.data.models.group.request.UpdateGroupRequest
import com.base.app.data.repositories.group.GroupRepository
import com.base.app.ui.group.detail_group.viewdata.DetailUpdateGroupInformationViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 11/16/2023
 */
@HiltViewModel
class UpdateGroupViewModel @Inject constructor(
    private val repository: GroupRepository
) : BaseViewModel() {
    var groupId = 0.toLong()

    private var _informationResponse: MutableLiveData<DetailUpdateGroupInformationViewData> =
        MutableLiveData()
    val informationResponse: LiveData<DetailUpdateGroupInformationViewData>
        get() = _informationResponse

    private var _uploadImageToStorage: MutableLiveData<Event<String>> = MutableLiveData()
    val uploadImageToStorage: LiveData<Event<String>>
        get() = _uploadImageToStorage

    private var _updateGroupResponse: MutableLiveData<Event<String>> = MutableLiveData()
    val updateGroupResponse: LiveData<Event<String>>
        get() = _updateGroupResponse

    fun getGroupInformation(groupId: Long) {
        viewModelScope.launch(handler) {
            val result = repository.getGroupById(groupId)
            val groupData = result.data
            val detailInformation = DetailUpdateGroupInformationViewData(
                groupImage = groupData?.groupImageUrl.toString(),
                groupName = groupData?.groupName.toString(),
                privacy = groupData?.groupPrivacy.toString(),
                groupDescription = groupData?.groupDescription.toString()
            )
            _informationResponse.value = detailInformation
        }
    }

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

    fun updateGroup(groupId: Long,request: UpdateGroupRequest) {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val result = repository.updateGroup(groupId,request)
            _updateGroupResponse.value = Event(result.status?.message.toString())
            registerJobFinish()
        }
    }
}