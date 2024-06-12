package com.da_chelimo.whisper.chats.presentation.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Interval

fun Long.toActualChatSeparatorTime(): String {
    val jodaTime = DateTime(this, DateTimeZone.getDefault())
    val timeInterval = Interval(jodaTime.millis, System.currentTimeMillis()).toDuration()

    val hoursOfTheDay = Interval(DateTime().withTimeAtStartOfDay().millis, System.currentTimeMillis()).toDuration()

    val isToday = timeInterval.isShorterThan(hoursOfTheDay)

    // Less than a day {it's better to use 18 instead of 24}
    return if (isToday) "Today"
    else if (timeInterval.standardDays < 2)
        "Yesterday"
    else if (timeInterval.standardDays < 7)
        jodaTime.toString("eeee")
    else
        jodaTime.toString("dd/MM/yyyy")
}

fun Long.toChatPreviewTime(): String {
    val jodaTime = DateTime(this, DateTimeZone.getDefault())
    val timeInterval = Interval(jodaTime.millis, System.currentTimeMillis()).toDuration()

    // Less than a day {it's better to use 18 instead of 24}
    return if (timeInterval.standardHours < 24)
        toHourAndMinute()
    else if (timeInterval.standardDays < 2)
        "Yesterday"
    else if (timeInterval.standardDays < 7)
        jodaTime.toString("eeee")
    else
        jodaTime.toString("dd/MM/yyyy")
}


fun Long.toHourAndMinute(): String {
    val jodaTime = DateTime(this, DateTimeZone.getDefault())
    return jodaTime.toString("HH:mm")
}