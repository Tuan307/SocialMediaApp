package com.base.app.data.models.request

import com.base.app.data.models.response.Message


data class ChatBotRequest(
    val model: String,
    val messages: List<Message>
)

