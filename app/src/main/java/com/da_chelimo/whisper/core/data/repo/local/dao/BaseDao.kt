package com.da_chelimo.whisper.core.data.repo.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertData(data: T)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertData(data: List<T>)

}