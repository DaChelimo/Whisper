package com.da_chelimo.whisper.chats.repo.audio_messages.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.da_chelimo.whisper.core.utils.StopWatch
import com.da_chelimo.whisper.core.utils.formatDurationInMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import timber.log.Timber

class AndroidAudioPlayer(
    private val stopWatch: StopWatch = StopWatch()
) : AudioPlayer {

    private var mediaPlayer: MediaPlayer? = null

    private val _audioBeingPlayed = MutableStateFlow<String?>(null)
    override val audioBeingPlayed: StateFlow<String?> = _audioBeingPlayed

    private val _playerState = MutableStateFlow(PlayerState.None)
    override val playerState: StateFlow<PlayerState> = _playerState

    // When null, timeLeftInMillis will be null; the duration of the audio recording is used by default in the UI
    private var audioDurationInMillis: Long? = null

    override val timeLeftInMillis = stopWatch.timeInMillis.map { timeUsedInMillis ->
        audioDurationInMillis
            ?.let { it - timeUsedInMillis }
            ?.formatDurationInMillis()
    }.onEach {
        Timber.d("timeLeftInMillis is $it")
    }


    private fun createAudioPlayer(audioUrl: String) =
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

            setDataSource(audioUrl)

            setOnCompletionListener {
                stopAudio()
            }
        }


    override suspend fun playAudio(context: Context, audioUrl: String) =
        withContext(Dispatchers.IO) {
            /**
             * Stops the current audio under two conditions if there was a vn being played earlier
             * and it is not the same as the one that is about to be played
             */
            if (audioBeingPlayed.value != null && audioUrl != audioBeingPlayed.value)
                stopAudio()

            if (audioBeingPlayed.value == null) { // Audio can only be played if no other audio is being played
                stopWatch.stopAndReset()

                _audioBeingPlayed.value = audioUrl
                _playerState.value = PlayerState.Ongoing

                createAudioPlayer(audioUrl).apply {
                    prepare()
                    start()

                    audioDurationInMillis = duration.toLong()

                    stopWatch.startOrResume()

                    mediaPlayer = this
                }
            }
        }


    override fun pauseAudio() {
        _playerState.value = PlayerState.Paused
        stopWatch.pauseOrStop()
        mediaPlayer?.pause()
    }

    override fun resumeAudio() {
        _playerState.value = PlayerState.Ongoing
        stopWatch.startOrResume()
        mediaPlayer?.start()
    }

    override fun stopAudio() {
        _audioBeingPlayed.value = null
        _playerState.value = PlayerState.None

        audioDurationInMillis = null
        stopWatch.stopAndReset()

        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}