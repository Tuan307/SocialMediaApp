package com.base.app.ui.profile_detail_post

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.R
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.apis.Api
import com.base.app.data.models.PostItem
import com.base.app.data.models.PushNotification
import com.base.app.data.models.User
import com.base.app.data.models.mToken
import com.base.app.data.models.response.post.ImageUserProfilePost
import com.base.app.data.repositories.profile.UserProfileRepository
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    private val api: Api,
    private val repository: UserProfileRepository
) : BaseViewModel() {
    private var dataListDetail = ArrayList<PostItem>()
    private var listResponseDetail = MutableLiveData<ArrayList<PostItem>>()
    val getListResponseDetail = listResponseDetail as LiveData<ArrayList<PostItem>>

    private var _detailUserPostResponse: MutableLiveData<ImageUserProfilePost> = MutableLiveData()
    val detailUserPostResponse: LiveData<ImageUserProfilePost>
        get() = _detailUserPostResponse

    fun getUserDetailPost(userId: String, pageCount: Int, pageNumber: Int) {
        showLoading(true)
        parentJob = viewModelScope.launch {
            val result = repository.getUserProfileImagePost(userId, pageCount, pageNumber)
            _detailUserPostResponse.value = result
            registerJobFinish()
        }
    }

    fun getDataDetail(id: String) {
        showLoading(true)
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Posts")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        dataListDetail.clear()
                        for (dataSnapshot in snapshot.children) {
                            val post = dataSnapshot.getValue(PostItem::class.java)
                            if (post != null && post.publicher == id) {
                                dataListDetail.add(post)
                            }
                        }
                        Handler(Looper.getMainLooper()).postDelayed({
                            listResponseDetail.postValue(dataListDetail)
                            registerJobFinish()
                        }, 1000)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("CheckAAA", "Yes Here")
                    }
                })
        }
    }

    fun likePost(postId: String, status: String, publisherId: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            if (status == "like") {
                databaseReference.child("Likes").child(postId).child(firebaseUser?.uid ?: "none")
                    .setValue(true)
                if (publisherId != firebaseUser?.uid.toString()) {
                    addNotifications(postId, publisherId)
                }
            } else {
                databaseReference.child("Likes").child(postId).child(firebaseUser?.uid ?: "none")
                    .removeValue()
            }
        }

    }

    private fun addNotifications(postId: String, publisherId: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            val hashMap = HashMap<String, Any>()
            hashMap["userid"] = firebaseUser?.uid.toString()
            hashMap["postid"] = postId
            hashMap["text"] = "liked your post!"
            hashMap["ispost"] = true
            databaseReference.child("Notifications").child(publisherId)
                .push().setValue(hashMap)
        }
    }

    fun savePost(postId: String, status: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            val uid = firebaseUser?.uid
            if (status == "save") {
                if (uid != null) {
                    databaseReference.child("Saves").child(uid).child(postId).setValue(true)
                }
            } else {
                if (uid != null) {
                    databaseReference.child("Saves").child(uid).child(postId).removeValue()
                }
            }
        }
    }

    fun isLikePost(postId: String, image: ImageView) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Likes").child(postId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val uid = firebaseUser?.uid
                        if (uid != null) {
                            if (snapshot.child(uid).exists()) {
                                image.tag = "liked"
                                image.setImageResource(R.drawable.ic_like)
                            } else {
                                image.setImageResource(R.drawable.ic_heart_border)
                                image.tag = "like"
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }

    }

    fun isSavedPost(postId: String, image: ImageView) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            val uid = firebaseUser?.uid
            if (uid != null) {
                databaseReference.child("Saves").child(uid)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.child(postId).exists()) {
                                image.tag = "saved"
                                image.setImageResource(R.drawable.ic_bookmark_boder_black)
                            } else {
                                image.setImageResource(R.drawable.ic_bookmark_border)
                                image.tag = "save"
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }
        }
    }

    var deletePostResponse = MutableLiveData<Boolean>()
    fun deletePost(postId: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Posts").child(postId).removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    deletePostResponse.postValue(true)
                } else {
                    deletePostResponse.postValue(false)
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

    fun countComments(text: TextView, postId: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Comments").child(postId)
                .addValueEventListener(object : ValueEventListener {
                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        text.text = "View all ${snapshot.childrenCount} comments"
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun getPostLikes(text: TextView, postId: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Likes").child(postId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val count = snapshot.childrenCount
                        text.text = "$count likes"
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun getPostDisLikes(text: TextView, postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Dislikes").child(postId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val count = snapshot.childrenCount
                        text.text = "$count dislikes"
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun getPublisherInformation(
        context: Context,
        imgAvatar: CircleImageView,
        txtUserName: TextView,
        txtPublisher: TextView,
        publisher: String?
    ) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            if (publisher != null) {
                databaseReference.child("Users").child(publisher)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            if (user != null) {
                                Glide.with(context).load(user.imageurl).into(imgAvatar)
                                txtUserName.text = user.username
                                txtPublisher.text = user.username
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }
        }
    }

    var userResponse = MutableLiveData<User>()
    fun getCurrentUserInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Users").child(firebaseUser?.uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)
                        if (user != null) {
                            userResponse.postValue(user)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }
}