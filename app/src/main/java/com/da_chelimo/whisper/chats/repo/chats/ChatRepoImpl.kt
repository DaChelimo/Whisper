package com.da_chelimo.whisper.chats.repo.chats

import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.CHATS_COLLECTION
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.CHAT_DETAILS
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.CHAT_MESSAGES
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.PERSONALIZED_CHATS
import com.da_chelimo.whisper.core.domain.MiniUser
import com.da_chelimo.whisper.core.domain.User
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.UUID

class ChatRepoImpl(private val userRepo: UserRepo = UserRepoImpl()) : ChatRepo {

    private val firestore = Firebase.firestore


    data class PersonalizedChat(
        val chatID: String
    ) {
        constructor() : this("")
    }

    override suspend fun getChatFromChatID(chatID: String): Chat? =
        firestore.collection(CHAT_DETAILS)
            .document(chatID)
            .get()
            .await()
            .toObject<Chat>()

    override suspend fun getChatsForUser(userID: String): List<Chat> {
        val listOfChatIDs = getPersonalizedChatsFirebaseRef(userID)
            .get()
            .await()
            .toObjects(PersonalizedChat::class.java)
            .map { it.chatID }

        if (listOfChatIDs.isEmpty())
            return emptyList()

        Timber.d("listOfChatIDs is $listOfChatIDs")

        val chats = firestore.collection(CHAT_DETAILS)
            .whereIn(Chat::chatID.name, listOfChatIDs)
            .orderBy(Chat::timeOfLastMessage.name, Query.Direction.DESCENDING)
            .get()
            .await()
            .toObjects(Chat::class.java)

        Timber.d("chats is $chats")
        return chats
    }

    override suspend fun createConversation(newContact: User): String {
        val chatID = UUID.randomUUID().toString()
        val currentUser = userRepo.getUserFromUID(Firebase.auth.uid!!)

        val chat = Chat(
            chatID = chatID,

            firstMiniUser = MiniUser(currentUser!!.name, currentUser.uid, currentUser.profilePic),
            secondMiniUser = MiniUser(newContact.name, newContact.uid, newContact.profilePic),

            unreadMessagesCount = 0,

            lastMessage = "",
            lastMessageSender = currentUser.uid,
            lastMessageStatus = MessageStatus.SENT,
            timeOfLastMessage = 1234455
        )

        //  chat_details >> chat1234
        firestore.collection(CHAT_DETAILS)
            .document(chatID)
            .set(chat)


        // Add to my individual list of chats
        // PERSONALIZED_CHATS >> uid1234  >> chat1234
        getPersonalizedChatsFirebaseRef(Firebase.auth.uid!!)
            .document(chatID)
            .set(PersonalizedChat(chatID))


        // Add to other individual list of chats
        // PERSONALIZED_CHATS >> FILLER >> uid0000 >> chat1234
        getPersonalizedChatsFirebaseRef(newContact.uid)
            .document(chatID)
            .set(PersonalizedChat(chatID))

        return chatID
    }

    private fun getPersonalizedChatsFirebaseRef(uid: String) =
        firestore.collection(PERSONALIZED_CHATS)
            .document("FILLER")
            .collection(uid)


    // chats >> chat1234 >> messages >> messageID
    override suspend fun sendMessage(chatID: String, message: Message): Boolean {
        return try {
            firestore.collection(CHATS_COLLECTION)
                .document(chatID)
                .collection(CHAT_MESSAGES)
                .document(message.messageID)
                .set(message)
                .addOnCompleteListener {
                    Timber.d("firestore.collection(CHATS_COLLECTION) is ${it.isSuccessful}")
                }
                .await()

            firestore.collection(CHATS_COLLECTION)
                .document(chatID)
                .collection(CHAT_MESSAGES)
                .document(message.messageID)
                .update(Message::messageStatus.name, MessageStatus.SENT)
                .addOnCompleteListener {
                    Timber.d("update(Message::messageStatus.name) is ${it.isSuccessful}")
                }
                .await()

            true
        } catch (e: Exception) {
            false
        }
    }


    // chats >> chat1234 >> messages
    override suspend fun getMessagesFromChatID(chatID: String) = callbackFlow<List<Message>> {

        val messagesSnapshotListener = firestore.collection(CHATS_COLLECTION)
            .document(chatID)
            .collection(CHAT_MESSAGES)
            .orderBy(Message::timeSent.name, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                val messages = value?.toObjects(Message::class.java)
                Timber.d("messages is $messages")
                Timber.e(error)

                trySend(messages ?: listOf())
            }

        awaitClose {
            messagesSnapshotListener.remove()
        }
    }


    override suspend fun updateMessageStatus(messageID: String, messageStatus: MessageStatus) {

    }
}