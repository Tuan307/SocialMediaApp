package com.base.app.ui.chat

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.apis.Api
import com.base.app.data.models.ChatModel
import com.base.app.data.models.PushNotification
import com.base.app.data.models.User
import com.base.app.data.models.mToken
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
    private val api: Api
) : BaseViewModel() {


    private var followerResponse = MutableLiveData<ArrayList<String>>()
    val getFollowerResponse = followerResponse as LiveData<ArrayList<String>>
    private var followerList: ArrayList<String> = ArrayList()
    fun getFollower() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Follow").child(firebaseUser?.uid.toString()).child("follower")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        followerList.clear()
                        for (data in snapshot.children) {
                            followerList.add(data.key.toString())
                        }
                        followerResponse.postValue(followerList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }


    private var userResponse = MutableLiveData<ArrayList<User>>()
    val getUserResponse = userResponse as LiveData<ArrayList<User>>
    private var userList: ArrayList<User> = ArrayList()
    fun getUserView(list: ArrayList<String>) {
        databaseReference.child("Users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()
                    for (data in snapshot.children) {
                        val user = data.getValue(User::class.java)
                        if (user != null) {
                            for (key in list) {
                                if (user.id == key) {
                                    userList.add(user)
                                }
                            }
                        }
                    }
                    userResponse.postValue(userList)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    var chatListResponse = MutableLiveData<ArrayList<ChatModel>>()
    private var chatList = ArrayList<ChatModel>()

    fun getPrivateChat(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Chats").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatList.clear()
                    for (data in snapshot.children) {
                        val chat = data.getValue(ChatModel::class.java)
                        if (chat != null) {
                            if ((chat.receiver == id && chat.sender == firebaseUser?.uid.toString()
                                        ) || (chat.sender == id && chat.receiver == firebaseUser?.uid.toString())
                            ) {
                                chatList.add(chat)
                            }
                        }
                    }
                    chatListResponse.postValue(chatList)
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
            databaseReference.child("Chats").child(key).setValue(hm).addOnSuccessListener {
                sendChatResponse.postValue(true)
            }.addOnFailureListener {
                sendChatResponse.postValue(false)
            }
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
}