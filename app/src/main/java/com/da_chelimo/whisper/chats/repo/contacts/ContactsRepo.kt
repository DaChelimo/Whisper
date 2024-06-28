package com.da_chelimo.whisper.chats.repo.contacts

import android.content.Context
import com.da_chelimo.whisper.core.domain.User
import kotlinx.coroutines.flow.Flow

interface ContactsRepo {

    /**
     * A flow with contacts on Whisper
     */
    val contactsOnWhisper: Flow<List<User>?>

    /**
     * Checks if the contact uid is in the user's chats already { There is a pre-existing convo }
     *
     * @param contactToCheck - the user to check
     *
     * @return chatID if existing chat is present; else, returns null
     */
    suspend fun checkForPreExistingChat(contactToCheck: User): String?


    /**
     * Fetches a list of the users contacts who are on Whisper and stores in Room DB
     */
    suspend fun refreshContactsOnWhisper(context: Context)


    /**
     * Gets the user details of a certain UID already stored in Room DB
     */
    suspend fun getContactFromUID(userUID: String): User?

}