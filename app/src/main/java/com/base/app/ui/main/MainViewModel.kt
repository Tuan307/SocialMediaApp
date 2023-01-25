package com.base.app.ui.main

import androidx.lifecycle.MutableLiveData
import com.base.app.CustomApplication.Companion.dataManager
import com.base.app.base.viewmodel.BaseViewModel

class MainViewModel : BaseViewModel() {
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
}