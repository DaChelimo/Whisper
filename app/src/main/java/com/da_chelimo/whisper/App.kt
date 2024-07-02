package com.da_chelimo.whisper

import android.app.Application
import com.da_chelimo.whisper.core.di.appModule
import com.da_chelimo.whisper.core.di.localModule
import com.da_chelimo.whisper.notifications.AppNotificationManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {
    private val appNotificationManager by lazy {
        AppNotificationManager(applicationContext)
    }

    /**
     * Issues:
     * 1) CreateProfile SelectPhoto causing crash
     * 2) Selecting user in ContactsScreen is causing nav.popStack()
     * 3) Change DP screen to use GlobalScope instead of viewModelScope
     * 4) Show loading spinner in the ContactsScreen when contacts have not yet
     *    been loaded
     * 5) Double sending of OTP code
     */

    /**
     * TODO
     * 1) Add user-activity (Active, Last active) -> DONE
     * 2) Add single, double and blue ticks in message -> DONE
     * 5) Fix navigation issues -> DONE
     *
     * 3) Video support
     * 4) Waveform in vns
     * 6) Status
     * 7) Calls
     */

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        appNotificationManager.clearNotifications()

//        networkIntent?.let {
//            try {
//                networkService.startService(it)
//            } catch (exception: Exception) {
//                Timber.e(exception)
//            }
//        }
//        networkMonitor.setupCallback(applicationContext, true)

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(appModule)
            modules(localModule)
        }
    }
}