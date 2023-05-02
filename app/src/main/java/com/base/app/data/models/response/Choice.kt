package com.base.app.data.models.response

import com.base.app.data.models.response.Message
import com.squareup.moshi.Json

data class Choice(
    val message: Message,
    @Json(name = "finish_reason")
    val finishReason: String,
    val index: Long
)