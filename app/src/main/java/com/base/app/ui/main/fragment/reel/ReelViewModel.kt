package com.base.app.ui.main.fragment.reel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
}