package com.da_chelimo.whisper.core.utils

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.flow.MutableStateFlow
import org.joda.time.Duration


fun Long.formatDurationInMillis(): String {
    val duration = Duration(this)
    val mins = duration.standardMinutes.let { if (it.toString().length == 1) "0$it" else it }
    val secs = duration.standardSeconds.let { if (it.toString().length == 1) "0$it" else it }

    return "$mins:$secs"
}


class StopWatch {
    var timeInMillis = MutableStateFlow(0L)

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