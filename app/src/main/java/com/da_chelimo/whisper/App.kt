package com.da_chelimo.whisper

import android.app.Application
import com.da_chelimo.whisper.core.di.appModule
import com.da_chelimo.whisper.core.di.localModule
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.d("Firebase.auth.uid is ${Firebase.auth.uid}")

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(appModule)
            modules(localModule)
        }
    }

}