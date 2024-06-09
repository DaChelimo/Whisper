package com.da_chelimo.whisper.chats.repo.contacts.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.da_chelimo.whisper.chats.repo.contacts.local.dao.LocalContactDao
import com.da_chelimo.whisper.chats.repo.contacts.local.entity.LocalContact

@TypeConverters(RoomTypeConverters::class)
@Database(entities = [LocalContact::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    // DAOs
    abstract val localContactDao: LocalContactDao

}