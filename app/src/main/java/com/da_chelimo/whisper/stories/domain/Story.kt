package com.da_chelimo.whisper.stories.domain

data class Story(
    val storyID: String,
    val authorUID: String,
    val imageUrl: String,
    val storyCaption: String,
    val timeUploaded: Long = System.currentTimeMillis(),
    val totalViewers: Int = 0,

    val viewersIDs: List<String> = listOf() // List<MiniUser>
) {

    constructor() : this(
        storyID = "",
        authorUID = "",
        imageUrl = "",
        storyCaption = "",
        timeUploaded = 0,
        totalViewers = 0,
        viewersIDs = listOf()
    )

    companion object {
        const val DURATION = 4000L
    }
}