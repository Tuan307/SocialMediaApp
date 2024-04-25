package com.base.app.ui.story.create_story_folder

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.request.AddStoryFolderRequest
import com.base.app.data.models.story.AddStoryFolderResponse
import com.base.app.data.repositories.story.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * @author tuanpham
 * @since 4/24/2024
 */

@HiltViewModel
class CreateStoryFolderViewModel @Inject constructor(
    private val repository: StoryRepository
) : BaseViewModel() {

    private var _addStoryFolderResponse: MutableLiveData<AddStoryFolderResponse> = MutableLiveData()
    val addStoryFolderResponse: LiveData<AddStoryFolderResponse>
        get() = _addStoryFolderResponse

    fun uploadImageToCloud(uri: Uri?, path: String?, name: String?) {
        uploadImageToFireBaseStorage(
            uri,
            path,
            name,
            viewModelScope,
            onSuccessUploaded = {
                viewModelScope.launch {
                    val result = repository.saveStoryFolder(
                        AddStoryFolderRequest(
                            name.toString(),
                            Calendar.getInstance().time.time.toString(),
                            it,
                            firebaseUser?.uid.toString()
                        )
                    )
                    _addStoryFolderResponse.value = result
                }
            },
            onErrorUploaded = {
                Log.d("CheckForError", it)
            }
        )
    }

}