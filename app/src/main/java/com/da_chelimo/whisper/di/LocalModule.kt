package com.da_chelimo.whisper.di

import android.content.Context
import androidx.room.Room
import com.da_chelimo.whisper.chats.repo.contacts.local.AppDatabase
import com.da_chelimo.whisper.chats.repo.contacts.local.dao.LocalContactDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {

    single<AppDatabase> { providesAppDatabase(androidContext()) }

    single<LocalContactDao> { providesLocalContactDao(get()) }
}

fun providesLocalContactDao(appDatabase: AppDatabase) = appDatabase.localContactDao


fun providesAppDatabase(context: Context) =
    Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "AppDatabase"
    ).fallbackToDestructiveMigration().build()