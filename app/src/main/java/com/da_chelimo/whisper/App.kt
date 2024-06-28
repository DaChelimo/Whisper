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
//
//    private val networkMonitor = NetworkMonitor()
//    private val networkService = NetworkService(networkMonitor)
//
//
//    private val networkIntent: Intent? by lazy {
//        Intent(applicationContext, NetworkService::class.java)
//    }

    private val appNotificationManager by lazy {
        AppNotificationManager(applicationContext)
    }

    /**
     * TODO
     * 1) Add user-activity (Active, Last active) -> DONE
     * 2) Add single, double and blue ticks in message -> DONE
     *
     * 3) Video support
     * 4) Waveform in vns
     * 5) Fix navigation issues
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