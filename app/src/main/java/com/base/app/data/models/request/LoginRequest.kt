package com.base.app.data.models.request


data class LoginRequest(
    var email: String,
    var password: String,
    var deviceId: String
)