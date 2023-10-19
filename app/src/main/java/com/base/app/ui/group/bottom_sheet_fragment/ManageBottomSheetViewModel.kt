package com.base.app.ui.group.bottom_sheet_fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.repositories.group.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/17/2023
 */
@HiltViewModel
class ManageBottomSheetViewModel @Inject constructor(
    private val repository: GroupRepository
) : BaseViewModel() {

    private var _deleteGroupResponse: MutableLiveData<Boolean> = MutableLiveData()
    val deleteGroupResponse: LiveData<Boolean>
        get() = _deleteGroupResponse

    fun deleteGroup(id: Long) {
        viewModelScope.launch {
            val result = repository.deleteGroup(id)
            _deleteGroupResponse.value = result.status.code == 200.toLong()
        }
    }
}