package com.da_chelimo.generate

import android.app.Application
import com.da_chelimo.generate.core.data.di.appModule
import com.da_chelimo.generate.core.data.di.localModule
import com.da_chelimo.generate.core.data.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(appModule, localModule, networkModule)
        }
    }

}