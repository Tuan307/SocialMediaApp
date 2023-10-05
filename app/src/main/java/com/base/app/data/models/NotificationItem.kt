package com.base.app.data.models

data class NotificationItem(
    var userid: String,
    var text: String,
    var imageUrl: String,
    var postid: String,
    var ispost: Boolean
) {
    constructor() : this("", "", "", "", false)
}