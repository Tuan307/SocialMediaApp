package com.base.app.ui.story.content_story

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.request.AddStoryContentRequest
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

    private var _addStoryContentResponse: MutableLiveData<Boolean> = MutableLiveData()
    val addStoryContentResponse: LiveData<Boolean>
        get() = _addStoryContentResponse

    fun fetchStoryContent(id: Int) {
        viewModelScope.launch {
            repository.getAllStoryContentFolder(id).collect {
                _storyContentResponse.value = it.data ?: emptyList()
            }
        }
    }

    fun uploadStoryContent(id: Int, uri: List<Uri>, path: List<String>) {
        viewModelScope.launch {
            uploadMultipleImageToFireBaseStorage(uri, path) {
                uploadStoryContentToBackend(id, it)
            }
        }
    }

    private fun uploadStoryContentToBackend(id: Int, list: List<String>) {
        viewModelScope.launch {
            val result = repository.saveStoryContentFolder(AddStoryContentRequest("0", id, list))
            if (result.data != null) {
                _addStoryContentResponse.value = true
                registerJobFinish()
            }
        }
    }
}