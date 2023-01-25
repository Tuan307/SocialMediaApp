package com.base.app.data.models

data class Comment(
    var comment: String?,
    val publisher: String?,
    val commentid: String?,
) {
    constructor() : this(null, null, null)
}