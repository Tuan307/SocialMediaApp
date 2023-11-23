package com.base.app.ui.main

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.CustomApplication.Companion.dataManager
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.User
import com.base.app.data.models.dating_app.UserUpdateProfileResponse
import com.base.app.data.models.mToken
import com.base.app.data.models.request.UpdateProfileRequest
import com.base.app.data.repositories.profile.UserProfileRepository
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UserProfileRepository
) : BaseViewModel() {

    private var _updateProfileRemote = MutableLiveData<UserUpdateProfileResponse>()
    val updateProfileRemote: LiveData<UserUpdateProfileResponse>
        get() = _updateProfileRemote

    fun updateProfile(request: UpdateProfileRequest) {
        showLoading(true)
        viewModelScope.launch(handler) {
            val result = repository.updateUserProfile(request)
            _updateProfileRemote.value = result
            Handler(Looper.getMainLooper()).postDelayed({
                registerJobFinish()
            }, 1000)
        }
    }


    var currentIndex = MutableLiveData<Int>(0)
    fun setCurrentIndex(i: Int) {
        currentIndex.postValue(i)
    }

    fun saveId() {
        dataManager.save("id", firebaseUser?.uid.toString())
    }

    var something = MutableLiveData<Boolean>()
    fun setSomething(t: Boolean) {
        something.postValue(t)
    }

    var doubleClick = MutableLiveData<Boolean>(false)
    fun setRefresh(t: Boolean) {
        doubleClick.postValue(t)
    }

    var reelClick = MutableLiveData<Int>(0)
    fun setReelClick() {
        reelClick.postValue(reelClick.value?.plus(1) ?: 1)
    }

    var callGetVideo = MutableLiveData<Boolean>()
    fun setUpIsReelClick(t: Boolean) {
        callGetVideo.postValue(t)
    }

    var userResponse = MutableLiveData<User>()
    fun getCurrentUserInformation() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Users").child(firebaseUser?.uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)
                        if (user != null) {
                            userResponse.postValue(user)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    //fake reel information data
    fun getUser(context: Context, image: CircleImageView, text1: TextView) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Users").child(firebaseUser?.uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(User::class.java)
                        if (user != null) {
                            Glide.with(context).load(user.imageurl).into(image)
                            text1.text = user.username

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun updateToken(token: String) {
        if (firebaseUser != null) {
            val mToken = mToken(token)
            databaseReference.child("Tokens").child(firebaseUser.uid).setValue(mToken)
        }
    }
}