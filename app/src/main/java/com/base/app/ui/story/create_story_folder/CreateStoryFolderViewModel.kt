package com.base.app.ui.story.create_story_folder

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel

/**
 * @author tuanpham
 * @since 4/24/2024
 */
class CreateStoryFolderViewModel : BaseViewModel() {

    fun uploadImageToCloud(uri: Uri?, path: String?, name: String?) {
        uploadImageToFireBaseStorage(
            uri,
            path,
            name,
            viewModelScope,
            onSuccessUploaded = {
                //upload to mysql database here
            },
            onErrorUploaded = {
                Log.d("CheckForError", it)
            }
        )
    }

}