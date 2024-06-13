package com.da_chelimo.whisper.chats.repo.messages

import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import kotlinx.coroutines.flow.Flow

interface MessagesRepo {


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


    /**
     * Edit messages
     */
    suspend fun editMessage(chatID: String, messageID: String, newMessage: String)

    /**
     * Unsends the message
     *
     * PS: Under the hood, deletes the message and checks:
     * 1) if it was unread. If yes, reduce unreadChatCount by 1
     * 2) if it was the last message. Update last message in ChatDetails
     */
    suspend fun unsendMessage(chatID: String, messageID: String): Boolean


    suspend fun updateMessageStatus(messageID: String, messageStatus: MessageStatus)
}