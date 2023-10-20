package com.base.app.ui.group.your_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.repositories.group.GroupRepository
import com.base.app.ui.group.your_group.viewdata.GroupBodyViewData
import com.base.app.ui.group.your_group.viewdata.GroupHeaderViewData
import com.base.app.ui.group.your_group.viewdata.GroupViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/12/2023
 */
@HiltViewModel
class GroupYourGroupViewModel @Inject constructor(
    private val groupRepository: GroupRepository
) : BaseViewModel() {

    private var _ownGroupListResponse: MutableLiveData<List<GroupViewData>> = MutableLiveData()
    val ownGroupListResponse: LiveData<List<GroupViewData>>
        get() = _ownGroupListResponse

    private var _joinedGroupListResponse: MutableLiveData<List<GroupViewData>> = MutableLiveData()
    val joinedGroupListResponse: LiveData<List<GroupViewData>>
        get() = _joinedGroupListResponse

    fun getGroup(type: Int) {
        viewModelScope.launch {
            val list = ArrayList<GroupViewData>()
            if (type == 0) {
                val result = groupRepository.getYourOwnGroup(firebaseUser?.uid.toString(), 5, 1)
                list.add(GroupHeaderViewData(0, "Nhóm của bạn"))
                result.data?.let {
                    list.addAll(it.map { data ->
                        GroupBodyViewData(
                            id = data.id ?: 0,
                            groupImage = data.groupImageUrl.toString(),
                            groupName = data.groupName.toString(),
                            groupJoinedDate = data.groupCreatedAt.toString()
                        )
                    })
                }
                _ownGroupListResponse.value = list
            } else {
                val result = groupRepository.getYourJoinedGroup(firebaseUser?.uid.toString(), 5, 1)
                list.add(GroupHeaderViewData(1, "Nhóm bạn đã tham gia"))
                result.data?.let {
                    list.addAll(it.map { data ->
                        GroupBodyViewData(
                            id = data.groupModelId.id ?: 0,
                            groupImage = data.groupModelId.groupImageUrl.toString(),
                            groupName = data.groupModelId.groupName.toString(),
                            groupJoinedDate = data.groupModelId.groupCreatedAt.toString()
                        )
                    })
                }
                _joinedGroupListResponse.value = list
            }
        }
    }

}