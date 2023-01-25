package com.base.app.ui.main.fragment.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : BaseViewModel() {

    var getUserResponse = MutableLiveData<ArrayList<User>>()
    private var list = ArrayList<User>()
    fun searchUser(s: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Users").orderByChild("username").startAt(s).endAt(s + "\uf8ff")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        list.clear()
                        for (data in snapshot.children) {
                            val user = data.getValue(User::class.java)
                            if (user != null) {
                                list.add(user)
                            }
                        }
                        getUserResponse.postValue(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }
}