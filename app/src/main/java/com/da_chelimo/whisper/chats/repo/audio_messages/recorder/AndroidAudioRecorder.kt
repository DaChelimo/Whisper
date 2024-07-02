package com.da_chelimo.whisper.chats.repo.audio_messages.recorder

import android.content.Context
import android.media.MediaRecorder
import android.media.MediaRecorder.AudioEncoder
import android.media.MediaRecorder.AudioSource
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import com.da_chelimo.whisper.core.utils.StopWatch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.util.UUID

class AndroidAudioRecorder(
    private val stopWatch: StopWatch = StopWatch()
) : AudioRecorder {

    private var mediaRecorder: MediaRecorder? = null
    private var cacheFile: File? = null

    override val durationInMillis: StateFlow<Long> = stopWatch.timeInMillis

    private val _recorderState = MutableStateFlow<RecorderState>(RecorderState.None)
    override val recorderState: StateFlow<RecorderState> = _recorderState


    private fun createAudioRecorder(context: Context, fileName: String): MediaRecorder {
        val mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            MediaRecorder(context)
        else MediaRecorder()

        return mediaRecorder.apply {
            setAudioSource(AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(AudioEncoder.HE_AAC)
            setOutputFile(fileName)
        }
    }

    override fun startRecording(context: Context) {
        cacheFile = File(context.cacheDir, "${UUID.randomUUID()}.mp3")

        createAudioRecorder(context, cacheFile?.path ?: return).apply {
            prepare()
            start()
            stopWatch.startOrResume()

            mediaRecorder = this
            _recorderState.value = RecorderState.Ongoing
        }
    }

    override fun pauseRecording() {
        _recorderState.value = RecorderState.Paused
        stopWatch.pauseOrStop()
        mediaRecorder?.pause()
    }

    override fun resumeRecording() {
        _recorderState.value = RecorderState.Ongoing
        stopWatch.startOrResume()
        mediaRecorder?.resume()
    }

    override fun endRecording(): Uri? {
        stopWatch.stopAndReset()

        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null

        return cacheFile?.toUri()?.apply {
            _recorderState.value = RecorderState.Ended(this)
            cacheFile = null
        }
    }

    override fun cancelRecording() {
        stopWatch.pauseOrStop()

        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null

        cacheFile?.delete()
        _recorderState.value = RecorderState.Ended(null)
    }


}