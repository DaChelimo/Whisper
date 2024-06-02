package com.da_chelimo.generate.core.data.di

import android.content.Context
import androidx.room.Room
import com.da_chelimo.generate.core.data.repo.local.dao.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {

    single<AppDatabase> { providesAppDatabase(androidContext()) }

}

fun providesAppDatabase(context: Context) =
    Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = "AppDatabase"
    ).fallbackToDestructiveMigration().build()