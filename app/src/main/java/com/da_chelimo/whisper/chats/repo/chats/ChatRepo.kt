package com.da_chelimo.whisper.chats.repo.chats

import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.core.domain.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

interface ChatRepo {

    data class PersonalizedChat(
        val chatID: String
    ) {
        constructor() : this("")
    }


    companion object {
        const val PERSONALIZED_CHATS = "personalized_chats"

        const val CHATS_COLLECTION = "chats"
        const val CHAT_DETAILS = "chat_details"
        const val CHAT_MESSAGES = "messages"

        fun getChatDetailsRef(chatID: String) =
            Firebase.firestore.collection(ChatRepo.CHAT_DETAILS).document(chatID)

        fun getPersonalizedChatsFirebaseRef(uid: String) =
            Firebase.firestore.collection(ChatRepo.PERSONALIZED_CHATS)
                .document("FILLER")
                .collection(uid)


        fun getMessagesCollectionRef(chatID: String) =
            Firebase.firestore.collection(ChatRepo.CHATS_COLLECTION)
                .document(chatID)
                .collection(ChatRepo.CHAT_MESSAGES)

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
    fun getChatsForUser(userID: String): Flow<List<Chat>>


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

    /**
     * Checks if the current user is the one who has pending unread messages
     *
     * If chats have been opened by the user who had unread messages,
     * reset the unreadMessagesCount to 0
     */
    suspend fun resetUnreadMessagesCount(chatID: String)

    suspend fun updateMessageStatus(messageID: String, messageStatus: MessageStatus)
}