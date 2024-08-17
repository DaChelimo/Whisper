package com.da_chelimo.whisper.chats.presentation.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Interval
import timber.log.Timber

fun Long.toActualChatSeparatorTime(): String? = try {
    val jodaTime = DateTime(this, DateTimeZone.getDefault())
    val timeInterval = Interval(jodaTime.millis, System.currentTimeMillis()).toDuration()

    val hoursOfTheDay =
        Interval(DateTime().withTimeAtStartOfDay().millis, System.currentTimeMillis()).toDuration()

    val isToday = timeInterval.isShorterThan(hoursOfTheDay)

    // Less than a day {it's better to use 18 instead of 24}

    if (isToday) "Today"
    else if (timeInterval.standardDays < 2)
        "Yesterday"
    else if (timeInterval.standardDays < 7)
        jodaTime.toString("EEEE")
    else
        jodaTime.toString("dd/MM/yyyy")
} catch (e: Exception) {
    Timber.e(e)
    null
}


fun Long.toChatPreviewTime(addAmPMSymbol: Boolean = false): String? = try {
    val jodaTime = DateTime(this, DateTimeZone.getDefault())
    val timeInterval = Interval(jodaTime.millis, System.currentTimeMillis()).toDuration()

//    Timber.d("LAst seen in millis is $this")
//    Timber.d("Last seen is ${DateTime(this).toString("HH:mm dd/MM/yyyy")}")
//    Timber.d("timeInterval.standardHours is ${timeInterval.standardHours}")
    // Less than a day {it's better to use 18 instead of 24}
    if (timeInterval.standardHours < 24)
        toHourAndMinute(addAmPMSymbol)
    else if (timeInterval.standardDays < 2)
        "Yesterday"
    else if (timeInterval.standardDays < 7)
        jodaTime.toString("EEEE")
    else
        jodaTime.toString("dd/MM/yyyy")
} catch (e: Exception) {
    Timber.e(e)
    null
}


fun Long.toHourAndMinute(addAmPMSymbol: Boolean = false): String {
    val jodaTime = DateTime(this, DateTimeZone.getDefault())
    return jodaTime.toString("HH:mm ${if (addAmPMSymbol) "a" else ""}")
}


fun Long.toDayMonthAndTime(): String {
    val jodaTime = DateTime(this, DateTimeZone.getDefault())
    return jodaTime.toString("dd MMM, HH:mm")
}