package com.base.app.ui.group.for_you

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.group.response.GetYourOwnGroupResponse
import com.base.app.data.models.response.post.ImagesList
import com.base.app.data.repositories.group.GroupRepository
import com.base.app.ui.group.for_you.viewdata.GroupForYouPostViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ocpsoft.prettytime.PrettyTime
import java.util.*
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 10/18/2023
 */
@HiltViewModel
class GroupForYouViewModel @Inject constructor(private val repository: GroupRepository) :
    BaseViewModel() {
    private var prettyTime = PrettyTime(Locale.getDefault())
    private var _groupYourGroupResponse: MutableLiveData<GetYourOwnGroupResponse> =
        MutableLiveData()
    val groupYourGroupResponse: LiveData<GetYourOwnGroupResponse>
        get() = _groupYourGroupResponse

    private var _groupGroupNewsFeedResponse: MutableLiveData<List<GroupForYouPostViewData>> =
        MutableLiveData()
    val groupGroupNewsFeedResponse: LiveData<List<GroupForYouPostViewData>>
        get() = _groupGroupNewsFeedResponse

    fun getYourGroup() {
        viewModelScope.launch(handler) {
            val result = repository.getYourOwnGroup(firebaseUser?.uid.toString(), 6, 1)
            _groupYourGroupResponse.value = result
        }
    }

    fun getGroupNewsFeed(count: Int, page: Int) {
        viewModelScope.launch(handler) {
            val result = repository.getAllGroupNewsFeed(count, page)
            _groupGroupNewsFeedResponse.value = result.data?.content?.map { data ->
                GroupForYouPostViewData(
                    id = data.groupPostId,
                    description = data.description,
                    itemList = data.groupPostContentItemList.map { it1 ->
                        ImagesList(it1.id, it1.imageUrl)
                    },
                    postUser = data.groupPostUserId,
                    postGroup = data.groupPostModelId,
                    createdAt =  prettyTime.format(Date(data.createdAt.toLong())),
                    address = data.checkInAddress,
                    latitude = data.checkInLatitude,
                    longitude = data.checkInLongitude,
                    type = data.type,
                    videoUrl = data.videoUrl,
                    question = data.question
                )
            }
        }
    }
}