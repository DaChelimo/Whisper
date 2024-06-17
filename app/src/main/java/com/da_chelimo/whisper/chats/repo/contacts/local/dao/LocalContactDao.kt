package com.da_chelimo.whisper.chats.repo.contacts.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.da_chelimo.whisper.chats.repo.contacts.local.entity.LocalContact
import com.da_chelimo.whisper.core.domain.User
import kotlinx.coroutines.flow.Flow

@Dao
abstract class LocalContactDao: BaseDao<LocalContact>() {


    @Query("SELECT contact FROM localcontact WHERE id = :userUID")
    abstract suspend fun getContact(userUID: String): User?

    @Query("SELECT contact FROM localcontact")
    abstract fun getContacts(): Flow<List<User>>

    @Query("DELETE FROM localcontact")
    abstract suspend fun deleteContacts()


    @Transaction
    open suspend fun refreshContacts(newContacts: List<LocalContact>) {
        deleteContacts()
        insertData(newContacts)
    }
}