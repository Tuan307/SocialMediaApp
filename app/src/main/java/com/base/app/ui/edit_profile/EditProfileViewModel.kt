package com.base.app.ui.edit_profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileViewModel : BaseViewModel() {

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

    private var updateProfileResponse = MutableLiveData<Boolean>()
    val getUpdateProfileResponse = updateProfileResponse as LiveData<Boolean>
    fun updateProfile(
        fullName: String,
        userName: String,
        bio: String,
        imageUri: Uri?,
        imagePath: String?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val hashMap = HashMap<String, Any>()
            hashMap["fullname"] = fullName
            hashMap["username"] = userName
            hashMap["bio"] = bio
            firebaseUser?.let {
                databaseReference.child("Users").child(it.uid).updateChildren(hashMap)
                    .addOnCompleteListener { it1 ->
                        if (it1.isSuccessful) {
                            updateProfileResponse.postValue(true)
                        } else {
                            updateProfileResponse.postValue(false)
                        }
                    }
            }
            if (imagePath != null && imageUri != null) {
                uploadAvatar(imageUri, imagePath)
            }
        }
    }

    var uploadImageResponse = MutableLiveData<Boolean>()
    private fun uploadAvatar(uri: Uri, path: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val ref = storageRef.child("uploads").child(
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
                    val hashMap = HashMap<String, Any>()
                    hashMap["imageurl"] = "" + downloadUri
                    databaseReference.child("Users").child(firebaseUser!!.uid)
                        .updateChildren(hashMap)
                    uploadImageResponse.postValue(true)
                } else {
                    uploadImageResponse.postValue(false)
                }
            }.addOnFailureListener {
                uploadImageResponse.postValue(false)
            }
        }
    }
}