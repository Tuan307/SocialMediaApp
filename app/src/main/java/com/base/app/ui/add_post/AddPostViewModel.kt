package com.base.app.ui.add_post

import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.group.request.CreateGroupPostRequest
import com.base.app.data.models.group.response.AddPostByGroupResponse
import com.base.app.data.models.request.PostNewsFeedRequest
import com.base.app.data.repositories.feed.NewsFeedRepository
import com.base.app.data.repositories.group.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddPostViewModel @Inject constructor(
    private val newsFeedRepository: NewsFeedRepository,
    private val groupRepository: GroupRepository
) : BaseViewModel() {

    var uploadImageResponse = MutableLiveData<Boolean>()

    private var _uploadImageList: MutableLiveData<PostNewsFeedRequest> = MutableLiveData()
    val uploadImageList: LiveData<PostNewsFeedRequest>
        get() = _uploadImageList

    private var _uploadGroupImagesList: MutableLiveData<CreateGroupPostRequest> = MutableLiveData()
    val uploadGroupImagesList: LiveData<CreateGroupPostRequest>
        get() = _uploadGroupImagesList

    private var _uploadGroupQuestion: MutableLiveData<CreateGroupPostRequest> = MutableLiveData()
    val uploadGroupQuestion: LiveData<CreateGroupPostRequest>
        get() = _uploadGroupQuestion

    private var _uploadGroupVideoResponse: MutableLiveData<CreateGroupPostRequest> =
        MutableLiveData()
    val uploadGroupVideoResponse: LiveData<CreateGroupPostRequest>
        get() = _uploadGroupVideoResponse

    var checkInAddress = ""
    var checkInLatitude = 0.0
    var checkInLongitude = 0.0
    var postNewsFeedRequest: PostNewsFeedRequest? = null
    var groupPostNewsFeedRequest: CreateGroupPostRequest? = null
    var postType = "question"

    private var _addGroupPostResponse: MutableLiveData<AddPostByGroupResponse> =
        MutableLiveData()
    val addGroupPostResponse: LiveData<AddPostByGroupResponse>
        get() = _addGroupPostResponse

    fun uploadQuestion(postNewsFeedRequest: PostNewsFeedRequest){
        showLoading(true)
        parentJob = viewModelScope.launch {
            val result = newsFeedRepository.addNewsPost(postNewsFeedRequest)
            if (result == null) {
                uploadImageResponse.postValue(false)
                registerJobFinish()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    uploadImageResponse.postValue(true)
                    registerJobFinish()
                }, 1000)
            }
        }
    }


    fun uploadImage(
        uri: List<Uri>,
        path: List<String>,
        description: String,
        from: String,
        groupId: Long
    ) {
        showLoading(true)
        if (uri.isNotEmpty() && path.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val updateList = arrayListOf<String>()
                val postId = databaseReference.push().key.toString()
                for (i in uri.indices) {
                    val ref = storageRef.child("new_posts").child(
                        System.currentTimeMillis().toString() + "." +
                                path
                    )
                    uploadTask = ref.putFile(uri[i])
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
                            updateList.add(downloadUri)
                            if (i == uri.size - 1) {
                                if (from == "group") {
                                    _uploadGroupImagesList.postValue(
                                        CreateGroupPostRequest(
                                            description,
                                            postId,
                                            updateList,
                                            firebaseUser?.uid.toString(),
                                            Calendar.getInstance().time.time.toString(),
                                            checkInAddress,
                                            checkInLatitude,
                                            checkInLongitude,
                                            "image",
                                            null,
                                            null,
                                            groupId
                                        )
                                    )
                                } else {
                                    _uploadImageList.postValue(
                                        PostNewsFeedRequest(
                                            description,
                                            postId,
                                            updateList,
                                            firebaseUser?.uid.toString(),
                                            Calendar.getInstance().time.time.toString(),
                                            checkInAddress,
                                            checkInLatitude,
                                            checkInLongitude,
                                            "image",
                                            null,
                                            null
                                        )
                                    )
                                }
                            }
                        } else {
                            uploadImageResponse.postValue(false)
                        }
                    }.addOnFailureListener {
                        uploadImageResponse.postValue(false)
                    }
                }
            }
        }
    }

    fun uploadImagePostToDB(postNewsFeedRequest: PostNewsFeedRequest) {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val result = newsFeedRepository.addNewsPost(postNewsFeedRequest)
            if (result == null) {
                uploadImageResponse.postValue(false)
                registerJobFinish()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    uploadImageResponse.postValue(true)
                    registerJobFinish()
                }, 1000)
            }
        }
    }

    fun uploadGroupPostToDB(request: CreateGroupPostRequest) {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val result = groupRepository.addGroupPost(request)
            if (result.data == null) {
                uploadImageResponse.postValue(false)
                registerJobFinish()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    uploadImageResponse.postValue(true)
                    registerJobFinish()
                }, 1000)
            }
        }
    }

    private var _uploadVideoResponse = MutableLiveData<PostNewsFeedRequest>()
    val uploadVideoResponse: LiveData<PostNewsFeedRequest>
        get() = _uploadVideoResponse

    fun uploadVideo(uri: Uri?, path: String?, description: String?, from: String, groupId: Long) {
        showLoading(true)
        if (uri != null && path != null) {
            parentJob = viewModelScope.launch(Dispatchers.IO) {
                val ref = storageRef.child("videos").child(
                    System.currentTimeMillis().toString() + "." +
                            path
                )
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
                        val postId = databaseReference.push().key.toString()
                        if (from == "home") {
                            _uploadVideoResponse.postValue(
                                PostNewsFeedRequest(
                                    description,
                                    postId,
                                    arrayListOf(),
                                    firebaseUser?.uid.toString(),
                                    Calendar.getInstance().time.time.toString(),
                                    checkInAddress,
                                    checkInLatitude,
                                    checkInLongitude,
                                    "video",
                                    downloadUri,
                                    null
                                )
                            )
                        } else {
                            _uploadGroupVideoResponse.postValue(
                                CreateGroupPostRequest(
                                    description,
                                    postId,
                                    arrayListOf(),
                                    firebaseUser?.uid.toString(),
                                    Calendar.getInstance().time.time.toString(),
                                    checkInAddress,
                                    checkInLatitude,
                                    checkInLongitude,
                                    "video",
                                    downloadUri,
                                    null,
                                    groupId
                                )
                            )
                        }
                    } else {
                        uploadImageResponse.postValue(false)
                    }
                }.addOnFailureListener {
                    uploadImageResponse.postValue(false)
                }
            }
        }
    }
}