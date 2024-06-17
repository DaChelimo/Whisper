package com.da_chelimo.whisper.chats.repo.chats

import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.core.domain.MiniUser
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
    suspend fun createConversation(newContact: MiniUser): String


    /**
     * Gets the details of the chat
     */
    suspend fun getChatFromChatID(chatID: String): Chat?


    /**
     * Gets all the chats of the user, given the userID
     */
    fun getChatsForUser(userID: String): Flow<List<Chat>?>

    /**
     * Disables all the user's chats
     * Process:
     * 1) Fetches all the chatIDs of the user
     * 2) Disables each of them
     */
    suspend fun disableChatsForUser(userID: String)

    /**
     * Disables a specific chat
     */
    fun disableChat(chatID: String)

    /**
     * Checks if the current user is the one who has pending unread messages
     *
     * If chats have been opened by the user who had unread messages,
     * reset the unreadMessagesCount to 0
     */
    suspend fun resetUnreadMessagesCount(chatID: String)

    suspend fun updateMessageStatus(messageID: String, messageStatus: MessageStatus)
}