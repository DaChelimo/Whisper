package com.da_chelimo.whisper.chats.repo.unread_messages

import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.repo.unread_messages.models.UnreadMessages
import kotlinx.coroutines.flow.Flow

interface UnreadMessagesRepo {


    fun getUnreadChatIDS(): Flow<List<String>>


    /**
     * Emits a flow of a list of the user's unread messages
     */
    fun getUnreadMessages(): Flow<List<UnreadMessages>>


    /**
     * Updates the given messages as RECEIVED for a specific chat
     */
    fun updateMessagesAsReceived(chatID: String, messages: List<Message>)


    /**
     * Mark all messages as opened if:
     * 1) it was not opened before AND
     * 2) it is from the other person
     */
    suspend fun updateMessagesAsOpened(chatID: String?)


    /**
     * Checks if the current user is the one who has pending unread messages
     *
     * If chats have been opened by the user who had unread messages,
     * reset the unreadMessagesCount to 0
     */
    suspend fun resetUnreadMessagesCount(chatID: String)

}