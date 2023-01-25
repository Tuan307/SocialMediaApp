package com.base.app.data.models.response

import com.base.app.data.models.response.login.Token
import com.base.app.data.models.response.login.User

data class LoginResponse(
    var token: Token? = null,
    var user: User? = null,
    val message: String? = null
)