package com.base.app.ui.follow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowerViewModel : BaseViewModel() {

    var followingResponse = MutableLiveData<ArrayList<String>>()
    private var list: ArrayList<String> = ArrayList()
    fun getFollowing(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Follow").child(id).child("following")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        list.clear()
                        for (data in snapshot.children) {
                            list.add(data.key.toString())
                        }
                        followingResponse.postValue(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

        }
    }

    var followerResponse = MutableLiveData<ArrayList<String>>()
    private var followerList: ArrayList<String> = ArrayList()
    fun getFollower(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Follow").child(id).child("follower")
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

    var userResponse = MutableLiveData<ArrayList<User>>()
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

    var getUserResponse = MutableLiveData<ArrayList<User>>()
    private var searchList = ArrayList<User>()
    fun searchUser(s: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchList.clear()
            for (data in userList) {
                if (data.username.toString().contains(s)) {
                    searchList.add(data)
                }
                getUserResponse.postValue(searchList)
            }

        }
    }
}