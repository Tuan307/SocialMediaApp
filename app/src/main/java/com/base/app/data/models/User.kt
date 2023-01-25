package com.base.app.data.models

data class User(
    val id: String? = null,
    val username: String? = null,
    val imageurl: String? = null,
    val bio: String? = null,
    val fullname: String? = null,
) {
    constructor() : this(null, null, null, null, null)
}