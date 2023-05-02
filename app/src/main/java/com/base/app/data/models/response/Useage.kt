package com.pet.app.data.models.response

import com.squareup.moshi.Json

data class Usage(
    @Json(name = "prompt_tokens")
    val promptTokens: Long,

    @Json(name = "completion_tokens")
    val completionTokens: Long,

    @Json(name = "total_tokens")
    val totalTokens: Long
)
