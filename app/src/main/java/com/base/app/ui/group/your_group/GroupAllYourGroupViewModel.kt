package com.base.app.ui.group.your_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.repositories.group.GroupRepository
import com.base.app.ui.group.your_group.viewdata.GroupBodyViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/12/2023
 */
@HiltViewModel
class GroupAllYourGroupViewModel @Inject constructor(
    private val repository: GroupRepository
) : BaseViewModel() {

    private var _ownGroupListResponse: MutableLiveData<List<GroupBodyViewData>> = MutableLiveData()
    val ownGroupListResponse: LiveData<List<GroupBodyViewData>>
        get() = _ownGroupListResponse

    private var _joinedGroupListResponse: MutableLiveData<List<GroupBodyViewData>> =
        MutableLiveData()
    val joinedGroupListResponse: LiveData<List<GroupBodyViewData>>
        get() = _joinedGroupListResponse

    private var _ownGroupListLoadMoreResponse: MutableLiveData<List<GroupBodyViewData>> =
        MutableLiveData()
    val ownGroupListLoadMoreResponse: LiveData<List<GroupBodyViewData>>
        get() = _ownGroupListLoadMoreResponse

    private var _joinedGroupListLoadMoreResponse: MutableLiveData<List<GroupBodyViewData>> =
        MutableLiveData()
    val joinedGroupListLoadMoreResponse: LiveData<List<GroupBodyViewData>>
        get() = _joinedGroupListLoadMoreResponse

    fun getGroup(type: Int, pageNumber: Int) {
        viewModelScope.launch {
            if (type == 0) {
                val result =
                    repository.getYourOwnGroup(firebaseUser?.uid.toString(), 10, pageNumber)
                if (pageNumber == 1) {
                    _ownGroupListResponse.value = result.data?.map { data ->
                        GroupBodyViewData(
                            id = data.id ?: 0,
                            groupImage = data.groupImageUrl.toString(),
                            groupName = data.groupName.toString(),
                            groupJoinedDate = data.groupCreatedAt.toString()
                        )
                    }
                } else {
                    _ownGroupListLoadMoreResponse.value = result.data?.map { data ->
                        GroupBodyViewData(
                            id = data.id ?: 0,
                            groupImage = data.groupImageUrl.toString(),
                            groupName = data.groupName.toString(),
                            groupJoinedDate = data.groupCreatedAt.toString()
                        )
                    }
                }
            } else {
                val result =
                    repository.getYourJoinedGroup(firebaseUser?.uid.toString(), 10, pageNumber)
                if (pageNumber == 1) {
                    _joinedGroupListResponse.value = result.data?.map { data ->
                        GroupBodyViewData(
                            id = data.id,
                            groupImage = data.groupModelId.groupImageUrl.toString(),
                            groupName = data.groupModelId.groupName.toString(),
                            groupJoinedDate = data.groupModelId.groupCreatedAt.toString()
                        )
                    }
                } else {
                    _joinedGroupListLoadMoreResponse.value = result.data?.map { data ->
                        GroupBodyViewData(
                            id = data.id,
                            groupImage = data.groupModelId.groupImageUrl.toString(),
                            groupName = data.groupModelId.groupName.toString(),
                            groupJoinedDate = data.groupModelId.groupCreatedAt.toString()
                        )
                    }
                }
            }
        }
    }
}