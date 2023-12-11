package com.base.app.ui.main.fragment.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.user.User
import com.base.app.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchForFriendViewModel @Inject constructor(
    private val repository: UserRepository
) : BaseViewModel() {

    private var _searchUserResponse: MutableLiveData<List<User>> = MutableLiveData()
    val searchUserResponse: LiveData<List<User>>
        get() = _searchUserResponse

    private var _searchUserLoadMoreResponse: MutableLiveData<List<User>> = MutableLiveData()
    val searchUserLoadMoreResponse: LiveData<List<User>>
        get() = _searchUserLoadMoreResponse

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