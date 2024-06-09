package com.da_chelimo.whisper.chats.repo.contacts

import android.content.Context
import com.da_chelimo.whisper.core.domain.User
import kotlinx.coroutines.flow.Flow

interface ContactsRepo {

    val contactsOnWhisper: Flow<List<User>>

    /**
     * Returns a list of the users contacts who are on Whisper
     */
    suspend fun getContactsOnWhisper(context: Context): List<User>


    suspend fun getContactFromUID(userUID: String): User?

}