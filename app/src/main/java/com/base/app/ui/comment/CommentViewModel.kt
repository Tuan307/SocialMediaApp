package com.base.app.ui.comment

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.apis.Api
import com.base.app.data.models.Comment
import com.base.app.data.models.PushNotification
import com.base.app.data.models.User
import com.base.app.data.models.mToken
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val api: Api
) : BaseViewModel() {

    var getImageResponse = MutableLiveData<User>()
    fun getImage() {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            firebaseUser?.let {
                databaseReference.child("Users").child(it.uid)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            if (user != null) {
                                getImageResponse.postValue(user)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }
        }
    }

    var userResponse: MutableLiveData<User> = MutableLiveData()
    fun getInformation(
        context: Context,
        image: ImageView,
        userName: TextView,
        publisherId: String
    ) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Users").child(publisherId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)
                        if (user != null) {
                            if (publisherId != firebaseUser?.uid.toString()) {
                                userResponse.postValue(user)
                            }
                            Glide.with(context).load(user.imageurl).into(image)
                            userName.text = user.username
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    var getCommentResponse = MutableLiveData<ArrayList<Comment>>()
    private var comments = ArrayList<Comment>()
    fun readComments(postId: String, from: String) {
        showLoading(true)
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            if (from == "home") {
                databaseReference.child("Comments").child(postId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            comments.clear()
                            for (data in snapshot.children) {
                                val comment = data.getValue(Comment::class.java)
                                if (comment != null) {
                                    comments.add(comment)
                                }
                            }
                            getCommentResponse.postValue(comments)
                            registerJobFinish()
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            } else {
                databaseReference.child("Group_Comments").child(postId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            comments.clear()
                            for (data in snapshot.children) {
                                val comment = data.getValue(Comment::class.java)
                                if (comment != null) {
                                    comments.add(comment)
                                }
                            }
                            getCommentResponse.postValue(comments)
                            registerJobFinish()
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }
        }
    }

    var addCommentResponse = MutableLiveData<Boolean>()
    fun addComments(postId: String, comment: String, from: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            val key = databaseReference.push().key.toString()
            val hashMap = HashMap<String, Any>()
            hashMap["comment"] = comment
            hashMap["publisher"] = firebaseUser!!.uid
            hashMap["commentid"] = key
            if (from == "home") {
                databaseReference.child("Comments").child(postId)
                    .child(key).setValue(hashMap).addOnCompleteListener {
                        if (it.isSuccessful) {
                            addCommentResponse.postValue(true)
                        } else {
                            addCommentResponse.postValue(false)
                        }
                    }
            } else {
                databaseReference.child("Group_Comments").child(postId)
                    .child(key).setValue(hashMap).addOnCompleteListener {
                        if (it.isSuccessful) {
                            addCommentResponse.postValue(true)
                        } else {
                            addCommentResponse.postValue(false)
                        }
                    }
            }
        }
    }

    private var _addReplyCommentResponse = MutableLiveData<Boolean>()
    val addReplyCommentResponse: LiveData<Boolean>
        get() = _addReplyCommentResponse

    fun addReplyComments(commentId: String, replyComment: String, from: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            val key = databaseReference.push().key.toString()
            val hashMap = HashMap<String, Any>()
            hashMap["comment"] = replyComment
            hashMap["publisher"] = firebaseUser!!.uid
            hashMap["commentid"] = key
            if (from == "home") {
                databaseReference.child("ReplyComment").child(commentId)
                    .child(key).setValue(hashMap).addOnCompleteListener {
                        if (it.isSuccessful) {
                            _addReplyCommentResponse.postValue(true)
                        } else {
                            _addReplyCommentResponse.postValue(false)
                        }
                    }
            } else {
                databaseReference.child("Group_ReplyComment").child(commentId)
                    .child(key).setValue(hashMap).addOnCompleteListener {
                        if (it.isSuccessful) {
                            _addReplyCommentResponse.postValue(true)
                        } else {
                            _addReplyCommentResponse.postValue(false)
                        }
                    }
            }
        }
    }

    fun getReplyCommentsNumber(
        commentId: String,
        text: AppCompatTextView,
        view: View,
        viewLine1: View,
        from: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if(from == "home"){
                databaseReference.child("ReplyComment").child(commentId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.childrenCount != 0.toLong()) {
                                text.isVisible = true
                                text.text = "Xem ${snapshot.childrenCount} phản hồi khác"
                                view.isVisible = true
                                viewLine1.isVisible = true
                            } else {
                                text.isVisible = false
                                view.isVisible = false
                                viewLine1.isVisible = false
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }else{
                databaseReference.child("Group_ReplyComment").child(commentId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.childrenCount != 0.toLong()) {
                                text.isVisible = true
                                text.text = "Xem ${snapshot.childrenCount} phản hồi khác"
                                view.isVisible = true
                                viewLine1.isVisible = true
                            } else {
                                text.isVisible = false
                                view.isVisible = false
                                viewLine1.isVisible = false
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }
        }
    }

    fun addNotifications(postId: String, comment: String, publisherId: String, imageUrl: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            val hashMap = HashMap<String, Any>()
            hashMap["userid"] = firebaseUser?.uid.toString()
            hashMap["postid"] = postId
            hashMap["imageUrl"] = imageUrl
            hashMap["text"] = "commented: $comment"
            hashMap["ispost"] = true
            databaseReference.child("Notifications").child(publisherId)
                .push().setValue(hashMap)
        }
    }

    var getVideoCommentResponse = MutableLiveData<ArrayList<Comment>>()
    private var videoComments = ArrayList<Comment>()
    fun getVideoComment(videoId: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("VideoComment").child(videoId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        videoComments.clear()
                        for (data in snapshot.children) {
                            val com = data.getValue(Comment::class.java)
                            if (com != null) {
                                videoComments.add(com)
                            }
                        }
                        getVideoCommentResponse.postValue(videoComments)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    var addVideoCommentResponse = MutableLiveData<Boolean>()
    fun addVideoComments(postId: String, comment: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            val key = databaseReference.push().key.toString()
            val hashMap = HashMap<String, Any>()
            hashMap["comment"] = comment
            hashMap["publisher"] = firebaseUser!!.uid
            hashMap["commentid"] = key
            databaseReference.child("VideoComment").child(postId)
                .child(key).setValue(hashMap).addOnCompleteListener {
                    if (it.isSuccessful) {
                        addVideoCommentResponse.postValue(true)
                    } else {
                        addVideoCommentResponse.postValue(false)
                    }
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