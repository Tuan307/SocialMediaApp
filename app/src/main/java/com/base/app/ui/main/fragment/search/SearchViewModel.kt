package com.base.app.ui.main.fragment.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.User
import com.base.app.data.models.dating_app.DatingUser
import com.base.app.data.repositories.UserRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchForFriendViewModel @Inject constructor(
    private val repository: UserRepository
) : BaseViewModel() {


    private var _searchUserResponse: MutableLiveData<List<DatingUser>> = MutableLiveData()
    val searchUserResponse: LiveData<List<DatingUser>>
        get() = _searchUserResponse

    private var _searchUserLoadMoreResponse: MutableLiveData<List<DatingUser>> = MutableLiveData()
    val searchUserLoadMoreResponse: LiveData<List<DatingUser>>
        get() = _searchUserLoadMoreResponse

//    fun setRecent(id: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            databaseReference.child("RecentSearch").child(firebaseUser?.uid.toString()).child(id)
//                .setValue(true)
//        }
//    }
//
//    fun removeRecent(id: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            databaseReference.child("RecentSearch").child(firebaseUser?.uid.toString()).child(id)
//                .removeValue()
//        }
//    }

    private var recentList = ArrayList<String>()
    var searchRecentKeyResponse: MutableLiveData<List<String>> = MutableLiveData()
    fun getRecentSearchKey() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("RecentSearch").child(firebaseUser?.uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        recentList.clear()
                        for (data in snapshot.children) {
                            recentList.add(data.key.toString())
                        }
                        searchRecentKeyResponse.postValue(recentList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    private var recentSearchList = ArrayList<User>()
    private var _recentSearchListResponse = MutableLiveData<List<DatingUser>>()
    val recentSearchListResponse: LiveData<List<DatingUser>>
        get() = _recentSearchListResponse

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
                        _recentSearchListResponse.postValue(recentSearchList.map {
                            DatingUser(
                                it.id.toString(),
                                it.username.toString(),
                                it.fullname.toString(),
                                it.imageurl.toString(),
                                it.bio,
                                "",
                                20.0,
                                20.0,
                                false,
                                 "",
                              false,
                                 null
                            )
                        })
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    fun searchUser(s: String, pageCount: Int, pageNumber: Int) {
        viewModelScope.launch(handler) {
            val result = repository.searchUsers(s, pageCount, pageNumber)
            if (pageNumber == 1) {
                _searchUserResponse.value = result.data
            } else {
                _searchUserLoadMoreResponse.value = result.data
            }
        }
    }
}