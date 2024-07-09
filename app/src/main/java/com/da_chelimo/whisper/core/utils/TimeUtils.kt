package com.da_chelimo.whisper.core.utils

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.da_chelimo.whisper.chats.presentation.utils.toHourAndMinute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.Interval


fun Long.toStoryTime(): String {
    val jodaTime = DateTime(this)
    val timeInterval = Interval(this, System.currentTimeMillis()).toDuration()

    return if (timeInterval.standardMinutes < 60)
        "${jodaTime.toString("mm")} minutes ago"
    else if (timeInterval.standardHours < 24)
        toHourAndMinute(true)
    else if (timeInterval.standardDays < 2)
        "Yesterday"
    else jodaTime.toString("HH:mm EEEE")
}


fun Long.formatDurationInMillis(): String {
    val duration = Duration(this)
    val mins = duration.standardMinutes.let { if (it.toString().length == 1) "0$it" else it }
    val secs = duration.standardSeconds.let { if (it.toString().length == 1) "0$it" else it }

    return "$mins:$secs"
}

class PlayerStopWatch {
    private val handler = Handler(Looper.getMainLooper())
    private var mediaPlayer: MediaPlayer? = null

    private val _timeInMillis = MutableStateFlow<Long?>(null)
    val timeInMillis: StateFlow<Long?> = _timeInMillis

    private var isPlaying = false

    private val runnable = object : Runnable {
        override fun run() {
            if (isPlaying) {
                mediaPlayer?.let {
                    _timeInMillis.value = (it.duration - it.currentPosition).toLong()
                    handler.postDelayed(this, 1000)
                }
            }
        }
    }

    fun start(currentMediaPlayer: MediaPlayer) {
        isPlaying = true
        mediaPlayer = currentMediaPlayer
        handler.post(runnable)
    }

    fun pause() { isPlaying = false }
    fun resume() { isPlaying = true; handler.post(runnable) }

    fun stop() {
        _timeInMillis.value = 0L
        mediaPlayer = null
    }
}


class RecorderStopWatch {
    val timeInMillis = MutableStateFlow(0L)

    private var isStopwatchRunning = false

    val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            if (isStopwatchRunning) {
                timeInMillis.value += 1000
                handler.postDelayed(this, 1000)
            }
        }
    }

    fun startOrResume() {
        isStopwatchRunning = true
        handler.post(runnable)
    }

    fun pauseOrStop() {
        isStopwatchRunning = false
    }

    fun stopAndReset() {
        timeInMillis.value = 0L
        isStopwatchRunning = false
    }
}