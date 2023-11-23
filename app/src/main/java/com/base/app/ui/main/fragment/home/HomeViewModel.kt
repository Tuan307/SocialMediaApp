package com.base.app.ui.main.fragment.home

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
import com.base.app.data.models.request.AddNotificationRequest
import com.base.app.data.models.request.SavedPostRequest
import com.base.app.data.models.response.post.ImagePostData
import com.base.app.data.models.response.post.PostContent
import com.base.app.data.repositories.feed.NewsFeedRepository
import com.base.app.data.repositories.notification.NotificationRepository
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel

class HomeViewModel @Inject constructor(
    private val api: Api,
    private val newsFeedRepository: NewsFeedRepository,
    private val notificationRepository: NotificationRepository
) : BaseViewModel() {

    private var _newFeedResponse: MutableLiveData<ImagePostData?> = MutableLiveData()
    val newFeedResponse: LiveData<ImagePostData?>
        get() = _newFeedResponse
    private var _newFeedLoadMoreResponse: MutableLiveData<ImagePostData?> = MutableLiveData()
    val newFeedLoadMoreResponse: LiveData<ImagePostData?>
        get() = _newFeedLoadMoreResponse

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


    private var dataList = ArrayList<PostItem>()
    private var listResponse = MutableLiveData<ArrayList<PostItem>>()
    val getListResponse = listResponse as LiveData<ArrayList<PostItem>>
    fun getData() {
        showLoading(true)
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Posts").orderByKey().limitToFirst(20)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        dataList.clear()
                        for (dataSnapshot in snapshot.children) {
                            val post = dataSnapshot.getValue(PostItem::class.java)
                            if (post != null) {
                                dataList.add(post)
                            }
                        }
                        Handler(Looper.getMainLooper()).postDelayed({
                            listResponse.postValue(dataList)
                            registerJobFinish()
                        }, 1000)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("CheckAAA", "Yes Here")
                    }
                })
        }
    }

    fun getNewsFeedData(pageCount: Int, pageNumber: Int) {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val result = newsFeedRepository.getNewsFeed(pageCount, pageNumber)
            if (pageNumber == 1) {
                _newFeedResponse.value = result.data
            } else {
                _newFeedLoadMoreResponse.value = result.data
            }
            Handler(Looper.getMainLooper()).postDelayed({
                registerJobFinish()
            }, 1500)
        }
    }

    private var _detailPostResponse: MutableLiveData<PostContent> = MutableLiveData()
    val detailPostResponse: LiveData<PostContent>
        get() = _detailPostResponse

    fun getDetailPost(postId: String) {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val result = newsFeedRepository.getDetailPost(postId)
            if (result != null) {
                _detailPostResponse.value = result[0]
            }
            registerJobFinish()
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
        viewModelScope.launch(handler) {
            notificationRepository.addNotification(
                AddNotificationRequest(
                    isPost = true,
                    isInvitation = false,
                    text = " đã thích bài viết của bạn",
                    ownerId = publisherId,
                    postId = postId,
                    timeStamp = Calendar.getInstance().time.time.toString(),
                    userId = firebaseUser?.uid.toString()
                )
            )
        }
    }

    fun savePost(postId: String) {
        viewModelScope.launch(handler) {
            newsFeedRepository.addSavedPost(
                SavedPostRequest(
                    firebaseUser?.uid.toString(),
                    postId
                )
            )
        }
//        parentJob = viewModelScope.launch(Dispatchers.IO) {
//            val uid = firebaseUser?.uid
//            if (status == "save") {
//                if (uid != null) {
//                    databaseReference.child("Saves").child(uid).child(postId).setValue(true)
//                }
//            } else {
//                if (uid != null) {
//                    databaseReference.child("Saves").child(uid).child(postId).removeValue()
//                }
//            }
//        }
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
        viewModelScope.launch(handler) {
            val result = newsFeedRepository.checkIfSavedPostExist(
                SavedPostRequest(
                    firebaseUser?.uid.toString(),
                    postId
                )
            )
            if (result.data != null) {
                image.tag = "saved"
                image.setImageResource(R.drawable.ic_bookmark_boder_black)
            } else {
                image.setImageResource(R.drawable.ic_bookmark_border)
                image.tag = "save"
            }
        }
    }

    private var _deletePostResponse = MutableLiveData<String>()
    val deletePostResponse: LiveData<String>
        get() = _deletePostResponse

    fun deletePost(postId: String) {
        showLoading(true)
        parentJob = viewModelScope.launch(handler) {
            val result = newsFeedRepository.deleteNewPost(postId)
            _deletePostResponse.value = result.status.message
            registerJobFinish()
        }
    }

    private var dataListDetail = ArrayList<PostItem>()
    private var listResponseDetail = MutableLiveData<ArrayList<PostItem>>()
    val getListResponseDetail = listResponseDetail as LiveData<ArrayList<PostItem>>
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