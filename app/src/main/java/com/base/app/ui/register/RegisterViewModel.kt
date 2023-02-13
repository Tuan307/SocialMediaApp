package com.base.app.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.common.IMAGE_URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterViewModel : BaseViewModel() {
    private var registerResponse = MutableLiveData<Boolean>()
    val getRegisterResponse = registerResponse as LiveData<Boolean>
    fun register(userName: String, fullName: String, email: String, password: String) {
        showLoading(true)
        parentJob = viewModelScope.launch(Dispatchers.IO) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val uid = firebaseUser?.uid.toString()
                    Log.d("CheckA", uid)
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["username"] = userName
                    hashMap["fullname"] = fullName
                    hashMap["bio"] = ""
                    hashMap["imageurl"] = IMAGE_URL
                    hashMap["id"] = uid
                    databaseReference.child("Users").child(uid).updateChildren(hashMap)
                        .addOnCompleteListener { it1 ->
                            if (it1.isSuccessful) {
                                registerResponse.postValue(true)
                            } else {
                                registerResponse.postValue(false)
                            }
                            registerJobFinish()
                        }
                } else {
                    registerResponse.postValue(false)
                    registerJobFinish()
                }
            }
        }
    }
}