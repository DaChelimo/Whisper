package com.da_chelimo.whisper

import android.app.Application
import com.da_chelimo.whisper.di.initKoin
import com.da_chelimo.whisper.notifications.AppNotificationManager
import timber.log.Timber

class App : Application() {

    private val appNotificationManager by lazy {
        AppNotificationManager(applicationContext)
    }

    /**
     * ASAP TODO:
     * 1) Add spinner in image when state is Not Sent -> DONE
     * 2) Create a not sent mini clock to put in a message when message is still sending -> DONE
     *
     * Issues:
     * 1) CreateProfile SelectPhoto causing crash -> FIXED
     * 2) Selecting user in ContactsScreen is causing nav.popStack()  -> FIXED
     * 3) Change DP screen to use GlobalScope instead of viewModelScope -> DONE
     * 4) Show loading spinner in the ContactsScreen when contacts have not yet
     *    been loaded
     * 5) Double sending of OTP code -> FIXED-ish
     * 6) Problems with voice call stopwatch
     */

    /**
     * TODO
     * 1) Add user-activity (Active, Last active) -> DONE
     * 2) Add single, double and blue ticks in message -> DONE
     * 5) Fix navigation issues -> DONE
     *
     * 6) Status -> ONGOING
     * 6b) Create a server that checks for expired stories (every hour) and deletes them
     *
     * 3) Video support
     * 4) Waveform in vns
     * 7) Calls
     */

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        initKoin()

        appNotificationManager.clearNotifications()
    }
}