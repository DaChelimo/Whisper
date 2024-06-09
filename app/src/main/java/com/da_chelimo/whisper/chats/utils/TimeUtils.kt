package com.da_chelimo.whisper.chats.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

fun Long.toHourAndMinute(): String {
    val jodaTime = DateTime(this, DateTimeZone.getDefault())
    return jodaTime.toString("HH:mm")
}