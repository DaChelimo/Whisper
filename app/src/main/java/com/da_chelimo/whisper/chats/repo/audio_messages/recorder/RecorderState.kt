package com.da_chelimo.whisper.chats.repo.audio_messages.recorder

import android.net.Uri

sealed class RecorderState {
    data object None: RecorderState()
    data object Ongoing: RecorderState()
    data object Paused: RecorderState()

    data class Ended(val fileUri: Uri?, val timeInMillis: Long): RecorderState()
}