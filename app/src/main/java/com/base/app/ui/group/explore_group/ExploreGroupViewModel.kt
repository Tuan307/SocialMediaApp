package com.base.app.ui.group.explore_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.group.response.GroupData
import com.base.app.data.repositories.group.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/19/2023
 */
@HiltViewModel
class ExploreGroupViewModel @Inject constructor(
    private val repository: GroupRepository
) : BaseViewModel() {

    private var _getAllGroupsResponse: MutableLiveData<List<GroupData>?> = MutableLiveData()
    val getAllGroupsResponse: LiveData<List<GroupData>?>
        get() = _getAllGroupsResponse

    fun getAllGroups(pageCount: Int, page: Int) {
        viewModelScope.launch {
            val result = repository.getAllGroups(firebaseUser?.uid.toString(), pageCount, page)
            _getAllGroupsResponse.value = result.data
        }
    }
}