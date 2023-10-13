package com.base.app.ui.group.detail_group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.ui.group.detail_group.viewdata.DetailGroupInformationViewData
import com.base.app.ui.group.detail_group.viewdata.DetailGroupPostViewData
import com.base.app.ui.group.detail_group.viewdata.DetailGroupViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/13/2023
 */
@HiltViewModel
class GroupDetailViewModel @Inject constructor() : BaseViewModel() {
    private var _listInformationResponse: MutableLiveData<List<DetailGroupViewData>> =
        MutableLiveData()
    val listInformationResponse: LiveData<List<DetailGroupViewData>>
        get() = _listInformationResponse

    private var _listGroupPostResponse: MutableLiveData<List<DetailGroupViewData>> =
        MutableLiveData()
    val listGroupPostResponse: LiveData<List<DetailGroupViewData>>
        get() = _listGroupPostResponse
    private val list: ArrayList<DetailGroupViewData> = arrayListOf()

    fun getGroupInformation() {
        viewModelScope.launch {
            list.clear()
            list.add(
                DetailGroupInformationViewData(
                    "1",
                    "https://firebasestorage.googleapis.com/v0/b/social-app-5e237.appspot.com/o/new_posts%2F1697045128322.jpg?alt=media&token=a7ee5a4d-d675-45f6-99f3-ef6ac427ef16",
                    "https://firebasestorage.googleapis.com/v0/b/social-app-5e237.appspot.com/o/new_posts%2F1697045128322.jpg?alt=media&token=a7ee5a4d-d675-45f6-99f3-ef6ac427ef16",
                    "ababaa1",
                    "private",
                    "12.000 thành viên",
                    true
                )
            )
            _listInformationResponse.value = list
        }
    }

    fun getGroupPost() {
        viewModelScope.launch {
            list.add(
                DetailGroupPostViewData(
                    "1",
                    "https://firebasestorage.googleapis.com/v0/b/social-app-5e237.appspot.com/o/new_posts%2F1697045128322.jpg?alt=media&token=a7ee5a4d-d675-45f6-99f3-ef6ac427ef16",
                    "https://firebasestorage.googleapis.com/v0/b/social-app-5e237.appspot.com/o/new_posts%2F1697045128322.jpg?alt=media&token=a7ee5a4d-d675-45f6-99f3-ef6ac427ef16",
                    arrayListOf("https://firebasestorage.googleapis.com/v0/b/social-app-5e237.appspot.com/o/new_posts%2F1697045128322.jpg?alt=media&token=a7ee5a4d-d675-45f6-99f3-ef6ac427ef16"),
                    "private",
                    "12.000 thành viên",
                    20.0,
                    20.0,
                    "image",
                    null,
                    null
                )
            )

            list.add(
                DetailGroupPostViewData(
                    "2",
                    "https://firebasestorage.googleapis.com/v0/b/social-app-5e237.appspot.com/o/new_posts%2F1697045128322.jpg?alt=media&token=a7ee5a4d-d675-45f6-99f3-ef6ac427ef16",
                    "https://firebasestorage.googleapis.com/v0/b/social-app-5e237.appspot.com/o/new_posts%2F1697045128322.jpg?alt=media&token=a7ee5a4d-d675-45f6-99f3-ef6ac427ef16",
                    arrayListOf("https://firebasestorage.googleapis.com/v0/b/social-app-5e237.appspot.com/o/new_posts%2F1697045128322.jpg?alt=media&token=a7ee5a4d-d675-45f6-99f3-ef6ac427ef16"),
                    "private",
                    "12.000 thành viên",
                    20.0,
                    20.0,
                    "image",
                    null,
                    null
                )
            )
            _listGroupPostResponse.value = list
        }
    }
}