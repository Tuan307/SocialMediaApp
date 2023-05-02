package com.base.app.data.models.response

import com.pet.app.data.models.response.Usage
import com.squareup.moshi.Json


data class ChatBotResponse(
    val id: String,
    @Json(name = "object")
    val welcome9Object: String,
    val created: Long,
    val model: String,
    val usage: Usage,
    val choices: List<Choice>
)



