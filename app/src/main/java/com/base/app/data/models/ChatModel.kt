package com.base.app.data.models

data class ChatModel(
    var id: String?,
    var message: String?,
    var sender: String?,
    var receiver: String?,
    var timestamp: String?
) {
    constructor() : this(null, null, null, null, null)
}