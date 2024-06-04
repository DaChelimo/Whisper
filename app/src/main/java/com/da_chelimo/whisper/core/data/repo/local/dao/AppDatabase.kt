package com.da_chelimo.whisper.core.data.repo.local.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.da_chelimo.whisper.core.data.repo.local.entity.LocalEntity

@Database(entities = [LocalEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {

    // DAOs


}