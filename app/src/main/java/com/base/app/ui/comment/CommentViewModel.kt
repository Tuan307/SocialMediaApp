package com.base.app.ui.comment

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.Comment
import com.base.app.data.models.User
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentViewModel : BaseViewModel() {

    var getImageResponse = MutableLiveData<String?>()
    fun getImage() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUser?.let {
                databaseReference.child("Users").child(it.uid)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            if (user != null) {
                                getImageResponse.postValue(user.imageurl)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }
        }
    }

    fun getInformation(
        context: Context,
        image: ImageView,
        userName: TextView,
        publisherId: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUser?.let {
                databaseReference.child("Users").child(publisherId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val user = snapshot.getValue(User::class.java)
                            if (user != null) {
                                Glide.with(context).load(user.imageurl).into(image)
                                userName.text = user.username
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }
        }
    }

    var getCommentResponse = MutableLiveData<ArrayList<Comment>>()
    private var comments = ArrayList<Comment>()
    fun readComments(postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
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
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    var addCommentResponse = MutableLiveData<Boolean>()
    fun addComments(postId: String, comment: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val key = databaseReference.push().key.toString()
            val hashMap = HashMap<String, Any>()
            hashMap["comment"] = comment
            hashMap["publisher"] = firebaseUser!!.uid
            hashMap["commentid"] = key
            databaseReference.child("Comments").child(postId)
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