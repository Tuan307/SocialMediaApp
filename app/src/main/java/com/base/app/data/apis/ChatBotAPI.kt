package com.base.app.data.apis

import com.base.app.data.models.request.ChatBotRequest
import com.base.app.data.models.response.ChatBotResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatBotAPI {
    @POST("chat/completions")
    suspend fun getChats(@Body message: ChatBotRequest): Response<ChatBotResponse>
}