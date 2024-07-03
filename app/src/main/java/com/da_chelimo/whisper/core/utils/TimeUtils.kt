package com.da_chelimo.whisper.core.utils

import android.os.Handler
import android.os.Looper
import com.da_chelimo.whisper.chats.presentation.utils.toHourAndMinute
import kotlinx.coroutines.flow.MutableStateFlow
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