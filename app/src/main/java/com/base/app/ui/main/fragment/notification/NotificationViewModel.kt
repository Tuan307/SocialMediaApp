package com.base.app.ui.main.fragment.notification

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.common.ERROR
import com.base.app.data.models.NotificationItem
import com.base.app.data.models.PostItem
import com.base.app.data.models.User
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel : BaseViewModel() {

    var listNotificationResponse = MutableLiveData<ArrayList<NotificationItem>>()
    private var list = ArrayList<NotificationItem>()
    fun readNotification() {
        showLoading(true)
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Notifications").child(firebaseUser?.uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        list.clear()
                        for (data in snapshot.children) {
                            val i = data.getValue(NotificationItem::class.java)
                            if (i != null) {
                                list.add(i)
                            }
                        }
                        listNotificationResponse.postValue(list)
                        registerJobFinish()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

        }
    }

    fun getUserInformation(
        context: Context,
        imageView: CircleImageView,
        textView: TextView,
        id: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Users").child(id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user: User? = snapshot.getValue(User::class.java)
                        if (user != null) {
                            textView.text = user.username
                            Glide.with(context).load(user.imageurl).into(imageView)
                        } else {
                            responseMessage.postValue(ERROR)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        responseMessage.postValue(ERROR)
                    }
                })
        }
    }

    fun getPostItem(context: Context, imageView: ImageView, postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Posts").child(postId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val post = snapshot.getValue(PostItem::class.java)
                        if (post != null) {
                            Glide.with(context).load(post.postimage).into(imageView)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }
}