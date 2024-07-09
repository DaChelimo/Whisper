package com.da_chelimo.whisper.chats.repo.messages

import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.domain.MessageType
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
     * Sends an image with or without a text message
     */
    suspend fun sendImageMessage(chatID: String, imageUri: String, messageText: String? = null): String?


    /**
     * Sends an audio with the audi file duration
     */
    suspend fun sendAudioMessage(chatID: String, audioUri: String, duration: Long): String?

    /**
     * Sends a message to the chatID
     *
     * @param messageType - The type of message being sent (contains any text message)
     * @param chatID - ID of the chat
     * @param finalMessageStatus - Allows us to leave a messagePreview(NOT_SENT) before the file (image or audio)
     *                        is done uploading
     *
     * TODO: Expand to allow for different media types by creating functions with similar parameters
     */
    suspend fun sendMessage(chatID: String, messageType: MessageType, finalMessageStatus: MessageStatus = MessageStatus.SENT): String?


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
}