package com.base.app.ui.story.content_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.story.StoryContentModel
import com.base.app.data.repositories.story.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 4/27/2024
 */
@HiltViewModel
class StoryContentViewModel @Inject constructor(
    private val repository: StoryRepository
) : BaseViewModel() {

    private var _storyContentResponse: MutableLiveData<List<StoryContentModel>> = MutableLiveData()
    val storyContentResponse: LiveData<List<StoryContentModel>>
        get() = _storyContentResponse

    fun fetchStoryContent(id: Int) {
        viewModelScope.launch {
            repository.getAllStoryContentFolder(id).collect {
                _storyContentResponse.value = it.data ?: emptyList()
            }
        }
    }

}