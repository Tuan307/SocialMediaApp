package com.base.app.ui.group.add_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.user.User
import com.base.app.data.models.group.InviteMember
import com.base.app.data.models.group.request.CreateGroupInvitationRequest
import com.base.app.data.models.group.response.CreateInvitationResponse
import com.base.app.data.repositories.UserRepository
import com.base.app.data.repositories.group.GroupRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/11/2023
 */
@HiltViewModel
class InviteMemberViewModel @Inject constructor(
    private val repository: UserRepository,
    private val groupRepository: GroupRepository
) : BaseViewModel() {

    private var _searchUserResponse: MutableLiveData<List<User>> = MutableLiveData()
    val searchUserResponse: LiveData<List<User>>
        get() = _searchUserResponse

    private var _searchUserLoadMoreResponse: MutableLiveData<List<User>> = MutableLiveData()
    val searchUserLoadMoreResponse: LiveData<List<User>>
        get() = _searchUserLoadMoreResponse

    private var _inviteUserResponse: MutableLiveData<CreateInvitationResponse> = MutableLiveData()
    val inviteUserResponse: LiveData<CreateInvitationResponse>
        get() = _inviteUserResponse


    fun inviteMember(request: CreateGroupInvitationRequest) {
        viewModelScope.launch(handler) {
            val result = groupRepository.createGroupInvitation(request)
            _inviteUserResponse.value = result
        }
    }

    var followingResponse = MutableLiveData<ArrayList<String>>()
    private var list: ArrayList<String> = ArrayList()
    fun getFollowing() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseReference.child("Follow").child(firebaseUser?.uid.toString()).child("following")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        list.clear()
                        for (data in snapshot.children) {
                            list.add(data.key.toString())
                        }
                        followingResponse.postValue(list)
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    var userResponse = MutableLiveData<ArrayList<InviteMember>>()
    private var userList: ArrayList<InviteMember> = ArrayList()
    fun getUserView(list: ArrayList<String>) {
        databaseReference.child("Users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userList.clear()
                    for (data in snapshot.children) {
                        val user = data.getValue(InviteMember::class.java)
                        if (user != null) {
                            for (key in list) {
                                if (user.id == key) {
                                    userList.add(user)
                                }
                            }
                        }
                    }
                    userResponse.postValue(userList)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
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