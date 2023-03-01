package com.base.app.ui.main.fragment.reel

import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.R
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.Video
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReelViewModel : BaseViewModel() {

    private var list: ArrayList<Video> = ArrayList()
    var videoListResponse = MutableLiveData<ArrayList<Video>>()
    fun getVideos() {
        showLoading(true)
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Videos")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        list.clear()
                        for (data in snapshot.children) {
                            val video = data.getValue(Video::class.java)
                            if (video != null) {
                                list.add(video)
                            }
                        }
                        Handler(Looper.getMainLooper()).postDelayed({
                            videoListResponse.postValue(list)
                            registerJobFinish()
                        }, 1000)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun clearAllData() {
        list.clear()
        videoListResponse.postValue(list)
    }

    fun likeVideo(videoId: String, status: String) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            if (status == "like") {
                databaseReference.child("VideoLike").child(videoId)
                    .child(firebaseUser?.uid.toString()).setValue(true)
            } else {
                databaseReference.child("VideoLike").child(videoId)
                    .child(firebaseUser?.uid.toString()).removeValue()
            }
        }
    }

    fun isLikeVideo(videoId: String, image: ImageView) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("VideoLike").child(videoId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val uid = firebaseUser?.uid.toString()
                        if (snapshot.child(uid).exists()) {
                            image.tag = "liked"
                            image.setImageResource(R.drawable.ic_like)

                        } else {
                            image.setImageResource(R.drawable.ic_heart_double_click)
                            image.tag = "like"
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun getVideoLikeCount(videoId: String, textView: TextView) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("VideoLike").child(videoId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val count = snapshot.childrenCount
                        textView.text = count.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun getVideoCommentCount(videoId: String, textView: TextView) {
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("VideoComment").child(videoId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val count = snapshot.childrenCount
                        textView.text = count.toString()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }


}