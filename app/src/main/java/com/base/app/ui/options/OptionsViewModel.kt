package com.base.app.ui.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.common.ERROR
import com.base.app.data.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OptionsViewModel : BaseViewModel() {

    private var userInformation = MutableLiveData<User?>()
    val getUserInformation = userInformation as LiveData<User?>
    fun getInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUser?.let {
                databaseReference.child("Users").child(it.uid)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            if (user != null) {
                                userInformation.postValue(user)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }
        }
    }

    var logOutResponse = MutableLiveData<Boolean>()
    fun logOut() {
        viewModelScope.launch(handler) {
            auth.signOut()
            logOutResponse.postValue(true)
        }
    }

    private var followerNumber = MutableLiveData<Long>()
    val getFollowerNumber = followerNumber as LiveData<Long>
    fun getFollower() {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Follow").child(firebaseUser?.uid.toString()).child("follower")
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
    fun getFollowing() {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Follow").child(firebaseUser?.uid.toString()).child("following")
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
}