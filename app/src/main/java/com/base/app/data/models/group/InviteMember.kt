package com.base.app.data.models.group

/**
 * @author tuanpham
 * @since 10/11/2023
 */
data class InviteMember(
    val id: String?,
    val username: String?,
    val imageurl: String?,
    val bio: String?,
    val fullname: String?,
    var hasInvite: Boolean = false
) {
    constructor() : this(null, null, null, null, null)
}

