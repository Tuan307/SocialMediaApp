package com.base.app.ui.add_video_post

import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddVideoViewModel : BaseViewModel() {

    var uploadVideoResponse = MutableLiveData<Boolean>()
    fun uploadVideo(uri: Uri?, path: String?) {
        showLoading(true)
        if (uri != null && path != null) {
            parentJob = viewModelScope.launch(Dispatchers.IO) {
                val ref = storageRef.child("videos").child(
                    System.currentTimeMillis().toString() + "." +
                            path
                )
                uploadTask = ref.putFile(uri)
                uploadTask!!.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    ref.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result.toString()
                        val postId = databaseReference.push().key.toString()
                        val hashMap = HashMap<String, Any>()
                        hashMap["videoId"] = postId
                        hashMap["id"] = firebaseUser?.uid.toString()
                        hashMap["url"] = downloadUri
                        databaseReference.child("Videos").child(postId).setValue(hashMap)
                        Handler(Looper.getMainLooper()).postDelayed({
                            uploadVideoResponse.postValue(true)
                            registerJobFinish()
                        }, 1000)
                    } else {
                        uploadVideoResponse.postValue(false)
                        registerJobFinish()
                    }
                }.addOnFailureListener {
                    uploadVideoResponse.postValue(false)
                    registerJobFinish()
                }
            }
        }
    }
}