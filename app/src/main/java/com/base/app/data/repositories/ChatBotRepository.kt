package com.base.app.data.repositories


import com.base.app.base.network.BaseRemoteService
import com.base.app.base.network.NetworkResult
import com.base.app.data.apis.ChatBotAPI
import com.base.app.data.models.request.ChatBotRequest
import com.base.app.data.models.response.Message
import com.base.app.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChatBotRepository @Inject constructor(
    private val api: ChatBotAPI,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : BaseRemoteService() {

    suspend fun getChat(request: Message): Message = withContext(dispatcher) {
        val model = "gpt-3.5-turbo"
        when (val result = callApi {
            api.getChats(
                ChatBotRequest(
                    model, listOf(request)
                )
            )
        }) {
            is NetworkResult.Success -> {
                result.data.choices[0].message
            }
            is NetworkResult.Error -> {
                throw result.exception
            }
        }

    }

}