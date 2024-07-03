package com.da_chelimo.whisper.stories.domain

import kotlinx.serialization.Serializable

@Serializable
data class StoryPreview(
    val authorID: String,
    val authorProfilePic: String?,
    val authorName: String,
    val storyCount: Int,
    val previewImage: String,
    val timeUploaded: Long
) {

    constructor(): this("",  null, "", 0, "", 0L)


    companion object {
        val TEST = StoryPreview().copy(authorName = "Bob Rob", authorProfilePic = null, timeUploaded = System.currentTimeMillis())
    }
}