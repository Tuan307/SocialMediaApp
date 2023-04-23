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
    fun setRecent(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("RecentSearch").child(firebaseUser?.uid.toString()).child(id)
                .setValue(true)
        }
    }

    fun removeRecent(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("RecentSearch").child(firebaseUser?.uid.toString()).child(id)
                .removeValue()
        }
    }

    private var recentList = ArrayList<String>()
    var searchKeyResponse: MutableLiveData<List<String>> = MutableLiveData()
    fun getRecentSearchKey() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("RecentSearch").child(firebaseUser?.uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        recentList.clear()
                        for (data in snapshot.children) {
                            recentList.add(data.key.toString())
                        }
                        searchKeyResponse.postValue(recentList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    private var recentSearchList = ArrayList<User>()
    fun getRecentSearch(keyList: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            recentSearchList.clear()
            databaseReference.child("Users")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            val user = data.getValue(User::class.java)
                            if (user != null) {
                                for (i in keyList) {
                                    if (user.id == i) {
                                        recentSearchList.add(user)
                                        break
                                    }
                                }
                            }
                        }
                        getUserResponse.postValue(recentSearchList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

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