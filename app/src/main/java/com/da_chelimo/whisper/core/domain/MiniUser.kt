package com.da_chelimo.whisper.core.domain

/**
 * Small, more compact version of User
 */
data class MiniUser(
    val name: String,
    val uid: String,
    val profilePic: String?,
    val lastSeen: Long = System.currentTimeMillis()
) {

    constructor(): this("", "", null, 0L)
}
