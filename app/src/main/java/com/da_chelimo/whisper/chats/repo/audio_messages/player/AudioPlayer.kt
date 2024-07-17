package com.da_chelimo.whisper.chats.repo.audio_messages.player

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayer {

    /**
     * Audio Url of the audio message being played
     *
     * null if not audio is being played
     */
    val audioBeingPlayed: StateFlow<String?>


    /**
     * State of the audio player
     */
    val playerState: StateFlow<PlayerState>


    /**
     * Time left before the end of the audio message is reached
     *
     * null if audio has not started being played;
     * when null, the length of the audio message is show
     */
    val timeLeftInMillis: Flow<Long?>

    suspend fun playAudio(context: Context, audioUrl: String)
    fun pauseAudio()
    fun resumeAudio()
    fun stopAudio()

    fun seekTo(newPositionInMillis: Long)
}