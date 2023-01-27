package com.base.app.ui.options

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
        viewModelScope.launch {
            auth.signOut()
            logOutResponse.postValue(true)
        }
    }
}