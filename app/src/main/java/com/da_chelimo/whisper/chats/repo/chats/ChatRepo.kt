package com.da_chelimo.whisper.chats.repo.chats

import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.core.domain.User
import kotlinx.coroutines.flow.Flow

interface ChatRepo {

    companion object {
        const val PERSONALIZED_CHATS = "personalized_chats"

        const val CHATS_COLLECTION = "chats"
        const val CHAT_DETAILS = "chat_details"
        const val CHAT_MESSAGES = "messages"
    }


    /**
     * Creates a new conversation. Updates the /chat/ collection and the individual collections
     *
     * @return String: The chatID of the new conversation
     */
    suspend fun createConversation(newContact: User): String


    /**
     * Gets the details of the chat
     */
    suspend fun getChatFromChatID(chatID: String): Chat?


    /**
     * Gets all the chats of the user, given the userID
     */
    suspend fun getChatsForUser(userID: String): List<Chat>



    /**
     * Fetches the messages in a given chat
     *
     * @param chatID - ID of the chat
     * @return A list of messages in the chat
     * TODO: Enhance this by paginating the messages
     */
    suspend fun getMessagesFromChatID(chatID: String): Flow<List<Message>>


    /**
     * Sends a message to the chatID
     *
     * @param message - The message being sent
     * @param chatID - ID of the chat
     *
     *
     * TODO: Expand to allow for different media types by creating functions with similar parameters
     */
    suspend fun sendMessage(chatID: String, message: Message): Boolean


    suspend fun updateMessageStatus(messageID: String, messageStatus: MessageStatus)
}