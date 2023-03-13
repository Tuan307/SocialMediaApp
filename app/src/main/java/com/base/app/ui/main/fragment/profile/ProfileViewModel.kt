package com.base.app.ui.main.fragment.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.CustomApplication.Companion.dataManager
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.common.ERROR
import com.base.app.data.models.PostItem
import com.base.app.data.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : BaseViewModel() {


    private var key = MutableLiveData<String?>()
    val getKey = key as LiveData<String?>
    fun getKey(t: Boolean) {
        if (t) {
            if (dataManager.getString("id") != null) {
                key.postValue(dataManager.getString("id"))
            }
        } else {
            key.postValue(firebaseUser?.uid.toString())
        }
    }

    var statusId = MutableLiveData<Boolean>()
    fun setId(id: String) {
        if (id == firebaseUser?.uid.toString()) {
            statusId.postValue(true)
        } else {
            statusId.postValue(false)
        }
    }

    private var userResponse = MutableLiveData<User?>()
    val getUserResponse = userResponse as LiveData<User?>
    fun getUserInformation(id: String) {
        showLoading(true)
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Users").child(id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user: User? = snapshot.getValue(User::class.java)
                        if (user != null) {
                            userResponse.postValue(user)
                        } else {
                            responseMessage.postValue(ERROR)
                        }
                        registerJobFinish()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        responseMessage.postValue(ERROR)
                    }
                })
        }
    }

    private var followerNumber = MutableLiveData<Long>()
    val getFollowerNumber = followerNumber as LiveData<Long>
    fun getFollower(id: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Follow").child(id).child("follower")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        followerNumber.postValue(snapshot.childrenCount)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        responseMessage.postValue(ERROR)
                    }
                })
        }
    }

    private var followingNumber = MutableLiveData<Long>()
    val getFollowingNumber = followingNumber as LiveData<Long>
    fun getFollowing(id: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {

            databaseReference.child("Follow").child(id).child("following")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        followingNumber.postValue(snapshot.childrenCount)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        responseMessage.postValue(ERROR)
                    }
                })
        }
    }

    private var profilePost = MutableLiveData<ArrayList<PostItem>>()
    private var list = ArrayList<PostItem>()
    val getProfilePost = profilePost as LiveData<ArrayList<PostItem>>
    fun getProfilePost(id: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {

            databaseReference.child("Posts").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (data in snapshot.children) {
                        val post = data.getValue(PostItem::class.java)
                        if (post != null) {
                            if (post.publicher == id) {
                                list.add(post)
                            }
                        }
                    }
                    profilePost.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    responseMessage.postValue(ERROR)
                }
            })
        }
    }

    private var keyListResponse = MutableLiveData<ArrayList<String>>()
    private var keyList = ArrayList<String>()
    val getKeyList = keyListResponse as LiveData<ArrayList<String>>
    fun getSavePostKey(id: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {

            databaseReference.child("Saves").child(id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        keyList.clear()
                        for (data in snapshot.children) {
                            keyList.add(data.key.toString())
                        }
                        keyListResponse.postValue(keyList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        responseMessage.postValue(ERROR)
                    }
                })
        }
    }

    private var savePost = MutableLiveData<ArrayList<PostItem>>()
    private var listSave = ArrayList<PostItem>()
    val getSavePost = savePost as LiveData<ArrayList<PostItem>>
    fun getSavePost(strings: ArrayList<String>) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Posts").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listSave.clear()
                    for (data in snapshot.children) {
                        val post = data.getValue(PostItem::class.java)
                        if (post != null) {
                            for (i in strings) {
                                if (i == post.postid) {
                                    listSave.add(post)
                                }
                            }

                        }
                    }
                    savePost.postValue(listSave)
                }

                override fun onCancelled(error: DatabaseError) {
                    responseMessage.postValue(ERROR)
                }
            })
        }
    }

    fun followUser(t: Boolean, id: String) {
        if (t) {
            databaseReference.child("Follow").child(firebaseUser?.uid.toString())
                .child("following")
                .child(id).setValue(true)
            databaseReference.child("Follow").child(id).child("follower")
                .child(firebaseUser?.uid.toString()).setValue(true)
        } else {
            databaseReference.child("Follow").child(firebaseUser?.uid.toString())
                .child("following")
                .child(id).removeValue()
            databaseReference.child("Follow").child(id).child("follower")
                .child(firebaseUser?.uid.toString()).removeValue()
        }
    }

    var followResponse = MutableLiveData<Boolean>()
    fun isFollowing(id: String) {
        if (id != "") {
            databaseReference.child("Follow").child(firebaseUser?.uid.toString())
                .child("following").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.child(id).exists()) {
                            followResponse.postValue(true)
                        } else {
                            followResponse.postValue(false)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun addNotifications(profileId: String) {
        val hm = HashMap<String, Any>()
        hm["userid"] = firebaseUser!!.uid
        hm["postid"] = ""
        hm["text"] = "started following you"
        hm["ispost"] = false
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Notifications").child(profileId).push().setValue(hm)
        }

    }
}