package com.base.app.data.models

data class Video(
    val id: String,//publisher id
    val url: String,
    val videoId: String,
    val description: String,
) {
    constructor() : this("", "", "", "")
}