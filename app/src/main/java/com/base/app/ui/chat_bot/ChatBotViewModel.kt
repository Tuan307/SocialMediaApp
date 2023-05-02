package com.base.app.ui.chat_bot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.base.app.base.viewmodel.BaseViewModel
import com.base.app.data.models.response.Message
import com.base.app.data.repositories.ChatBotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatBotViewModel @Inject constructor(
    private val chatBotRepository: ChatBotRepository
) : BaseViewModel() {

    private var _chatResponse = MutableLiveData<Message>()
    val chatResponse = _chatResponse as LiveData<Message>
    fun sendChatRequest(message: Message) {
        parentJob = viewModelScope.launch(handler) {
            val itemMessage = chatBotRepository.getChat(message)
            _chatResponse.postValue(itemMessage)
        }
        registerJobFinish()
    }

}