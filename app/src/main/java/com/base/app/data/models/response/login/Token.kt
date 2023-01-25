package com.base.app.data.models.response.login

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("accessToken")
    var accessToken: String? = null
)