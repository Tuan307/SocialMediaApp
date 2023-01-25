package com.base.app.ui.main.fragment.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.R
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.PostItem
import com.base.app.data.models.User
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {

    fun countComments(text: TextView, postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
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
                        listResponse.postValue(dataList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("CheckAAA", "Yes Here")
                    }
                })
        }
        registerJobFinish()
    }

    var getListOnLoadMore = MutableLiveData<ArrayList<PostItem>>()
    private var loadMoreList = ArrayList<PostItem>()
    fun getDataOnLoadMore(key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Posts").orderByKey().startAfter(key).limitToFirst(20)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        loadMoreList.clear()
                        for (dataSnapshot in snapshot.children) {
                            val post = dataSnapshot.getValue(PostItem::class.java)
                            if (post?.publicher != null) {
                                loadMoreList.add(post)
                            }
                        }
                        getListOnLoadMore.postValue(loadMoreList)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }

    var getLastKey = MutableLiveData<String>()
    fun getLastKey() {
        viewModelScope.launch(Dispatchers.IO) {
            val query = databaseReference.child("Posts").orderByKey().limitToLast(1)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        getLastKey.postValue(dataSnapshot.key)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

    }

    fun likePost(postId: String, status: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (status == "like") {
                databaseReference.child("Likes").child(postId).child(firebaseUser?.uid ?: "none")
                    .setValue(true)
                //TO DO add notifications here
                //code later
            } else {
                databaseReference.child("Likes").child(postId).child(firebaseUser?.uid ?: "none")
                    .removeValue()
            }
        }

    }

    fun savePost(postId: String, status: String) {
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
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
}