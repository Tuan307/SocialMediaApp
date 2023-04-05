//package com.base.app.ui.add_post
//
//import android.net.Uri
//import android.os.Handler
//import android.os.Looper
//import android.util.Log
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.base.app.base.viewmodel.BaseViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class AddPostViewModel : BaseViewModel() {
//
//    var uploadImageResource = MutableLiveData<ArrayList<String>>()
//    private var list: ArrayList<String> = ArrayList()
//    fun uploadImage(uri: Uri?, path: String?, description: String) {
//        showLoading(true)
//        if (uri != null && path != null) {
//            list.clear()
//            Log.d("CheckHere11", "Yes here")
//
//            parentJob = viewModelScope.launch(Dispatchers.IO) {
//                for (i in 0 until uri.size) {
//                    val ref = storageRef.child("new_posts").child(
//                        System.currentTimeMillis().toString() + "." +
//                                path[i]
//                    )
//                    uploadTask = uri[i]?.let { ref.putFile(it) }
//                    uploadTask!!.continueWithTask { task ->
//                        if (!task.isSuccessful) {
//                            task.exception?.let {
//                                throw it
//                            }
//                        }
//                        ref.downloadUrl
//                    }.addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            val downloadUri = task.result.toString()
//                            list.add(downloadUri)
//                            Log.d("CheckHere111", "Yes here ${list.size}")
//                        }
//                    }.addOnFailureListener {
//                        //uploadImageResponse.postValue(false)
//                        registerJobFinish()
//                    }
//                }
//                Log.d("CheckHere2", "Yes here ${list.size}")
//                uploadImageResource.postValue(list)
//            }
//
//        }
//    }
//
//    var uploadImageResponse = MutableLiveData<Boolean>()
//
//    fun uploadPostToServer(list: ArrayList<String>, description: String) {
//        viewModelScope.launch {
//            val postId = databaseReference.push().key.toString()
//            val hashMap = HashMap<String, Any>()
//            hashMap["postid"] = postId
//            hashMap["postimage"] = list
//            hashMap["description"] = description
//            hashMap["publicher"] = firebaseUser?.uid.toString()
//            databaseReference.child("Posts").child(postId).setValue(hashMap).addOnSuccessListener {
//                Handler(Looper.getMainLooper()).postDelayed({
//                    uploadImageResponse.postValue(true)
//                    registerJobFinish()
//                }, 1000)
//            }.addOnFailureListener {
//                uploadImageResponse.postValue(false)
//                registerJobFinish()
//            }
//        }
//    }
//}


package com.base.app.ui.add_post

import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddPostViewModel : BaseViewModel() {

    var uploadImageResponse = MutableLiveData<Boolean>()
    fun uploadImage(uri: Uri?, path: String?, description: String) {
        showLoading(true)
        if (uri != null && path != null) {
            parentJob = viewModelScope.launch(Dispatchers.IO) {
                val ref = storageRef.child("new_posts").child(
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
                        hashMap["postid"] = postId
                        hashMap["postimage"] = downloadUri
                        hashMap["description"] = description
                        hashMap["publicher"] = firebaseUser?.uid.toString()
                        databaseReference.child("Posts").child(postId).setValue(hashMap)
                        Handler(Looper.getMainLooper()).postDelayed({
                            uploadImageResponse.postValue(true)
                            registerJobFinish()
                        }, 1000)
                    } else {
                        uploadImageResponse.postValue(false)
                        registerJobFinish()
                    }
                }.addOnFailureListener {
                    uploadImageResponse.postValue(false)
                    registerJobFinish()
                }
            }
        }
    }
}