package com.da_chelimo.whisper.di

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

fun Application.initKoin() {
    startKoin {
        androidContext(applicationContext)
        androidLogger(Level.DEBUG)
        modules(appModule, viewModelModule, localModule, repoModule)
    }
}


val appModule = module {
    single<Moshi> { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

}