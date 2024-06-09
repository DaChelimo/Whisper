package com.da_chelimo.whisper.core.domain

/**
 * Small, more compact version of User
 */
data class MiniUser(
    val name: String,
    val uid: String,
    val profilePic: String?,
) {

    constructor(): this("", "", null)

}
