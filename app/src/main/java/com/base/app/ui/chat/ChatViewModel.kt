package com.base.app.ui.chat

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.apis.Api
import com.base.app.data.models.ChatModel
import com.base.app.data.models.PushNotification
import com.base.app.data.models.User
import com.base.app.data.models.mToken
import com.base.app.data.models.response.ListFollowResponse
import com.base.app.data.repositories.UserRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val api: Api,
    private val repository: UserRepository
) : BaseViewModel() {

    private var _isLoadingResponse: MutableLiveData<Boolean> = MutableLiveData()
    val isLoadingResponse: LiveData<Boolean>
        get() = _isLoadingResponse
    private var followerResponse = MutableLiveData<ArrayList<String>>()
    val getFollowerResponse = followerResponse as LiveData<ArrayList<String>>
    private var followerList: ArrayList<String> = ArrayList()

    private var _chatListUserResponse: MutableLiveData<ListFollowResponse> = MutableLiveData()
    val chatListUserResponse: LiveData<ListFollowResponse>
        get() = _chatListUserResponse

    fun getChatList() {
        viewModelScope.launch(handler) {
            val result = repository.getFollowList(firebaseUser?.uid.toString(), "follower")
            _chatListUserResponse.value = result
        }
    }

    private var _chatListResponse = MutableLiveData<ArrayList<ChatModel>>()
    val chatListResponse = _chatListResponse as LiveData<ArrayList<ChatModel>>
    private var chatList = ArrayList<ChatModel>()
    fun getPrivateChat(id: String, uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Chats").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatList.clear()
                    for (data in snapshot.children) {
                        val chat = data.getValue(ChatModel::class.java)
                        if (chat != null) {
                            if ((chat.receiver == id && chat.sender == uid)
                                || (chat.sender == id && chat.receiver == uid)
                            ) {
                                chatList.add(chat)
                            }
                        }
                    }
                    _chatListResponse.postValue(chatList)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }

    var sendChatResponse = MutableLiveData<Boolean>()

    @SuppressLint("SimpleDateFormat")
    fun sendMessage(message: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("hh:mm:ss aa dd/MM/yyyy")
            val time = sdf.format(calendar.time)
            val key = databaseReference.child("Chats").push().key.toString()
            val hm = HashMap<String, String>()
            hm["id"] = key
            hm["message"] = message
            hm["sender"] = firebaseUser?.uid.toString()
            hm["timestamp"] = time
            hm["receiver"] = id
            hm["type"] = "message"
            hm["imageUrl"] = ""
            databaseReference.child("Chats").child(key).setValue(hm).addOnSuccessListener {
                sendChatResponse.postValue(true)
            }.addOnFailureListener {
                sendChatResponse.postValue(false)
            }
        }
    }

    var uploadImageMessageResponse = MutableLiveData<Boolean>()

    @SuppressLint("SimpleDateFormat")
    fun uploadImageMessage(uri: Uri?, id: String) {
        _isLoadingResponse.value = true
        if (uri != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val ref =
                    storageRef.child("message_posts").child(System.currentTimeMillis().toString())
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
                        val postId = databaseReference.child("Chats").push().key.toString()
                        val hm = HashMap<String, String>()
                        val calendar = Calendar.getInstance()
                        val sdf = SimpleDateFormat("hh:mm:ss aa dd/MM/yyyy")
                        val time = sdf.format(calendar.time)
                        Log.d("CheckHere", downloadUri)
                        hm["id"] = postId
                        hm["message"] = ""
                        hm["sender"] = firebaseUser?.uid.toString()
                        hm["timestamp"] = time
                        hm["receiver"] = id
                        hm["type"] = "image"
                        hm["imageUrl"] = downloadUri
                        databaseReference.child("Chats").child(postId).setValue(hm)
                            .addOnSuccessListener {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    uploadImageMessageResponse.postValue(true)
                                    _isLoadingResponse.value = false
                                }, 1000)
                            }.addOnFailureListener {
                                uploadImageMessageResponse.postValue(false)
                                _isLoadingResponse.value = false
                            }
                    } else {
                        Log.d("CheckHere1", "downloadUri")
                        uploadImageMessageResponse.postValue(false)
                        _isLoadingResponse.value = false
                    }
                }.addOnFailureListener {
                    uploadImageMessageResponse.postValue(false)
                    _isLoadingResponse.value = false
                }
            }
        } else {
            _isLoadingResponse.value = false
        }
    }

    var sendNotificationResponse = MutableLiveData<Boolean>()
    fun sendNotification(notification: PushNotification) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = api.postNotification(notification)
            if (response.isSuccessful) {
                sendNotificationResponse.postValue(true)
            } else {
                sendNotificationResponse.postValue(false)
            }
        }
    }

    var tokenResponse = MutableLiveData<String>()
    fun getReceiverToken(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Tokens").child(id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val token = snapshot.getValue(mToken::class.java)
                        if (token != null) {
                            tokenResponse.postValue(token.token)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    private var _listOfImages = MutableLiveData<List<String>>()
    val listOfImages: LiveData<List<String>>
        get() = _listOfImages

    fun getAllImagesFromGallery(context: Context) {
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor: Cursor?
        val listOfAllImages = ArrayList<String>()
        var absolutePathOfImage: String? = null

        val projection =
            arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        cursor = context.contentResolver!!.query(uri, projection, null, null, null)

        val columnIndexData: Int = cursor!!.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        val columnIndexFolderName: Int = cursor!!
            .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor!!.moveToNext()) {
            absolutePathOfImage = cursor!!.getString(columnIndexData)
            listOfAllImages.add(absolutePathOfImage)
        }
        _listOfImages.value = listOfAllImages.reversed()
    }
}