package com.da_chelimo.whisper.chats.repo.audio_messages.recorder

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.flow.StateFlow

interface AudioRecorder {

    val recorderState: StateFlow<RecorderState>
    val durationInMillis: StateFlow<Long>

    fun startRecording(context: Context)
    fun endRecording(): Uri?

    fun pauseRecording()
    fun resumeRecording()

    fun cancelRecording()

}