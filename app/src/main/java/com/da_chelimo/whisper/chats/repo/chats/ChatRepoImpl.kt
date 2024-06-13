package com.da_chelimo.whisper.chats.repo.chats

import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.CHAT_DETAILS
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.getChatDetailsRef
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.getMessagesCollectionRef
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.getPersonalizedChatsFirebaseRef
import com.da_chelimo.whisper.core.domain.MiniUser
import com.da_chelimo.whisper.core.domain.User
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID

class ChatRepoImpl(private val userRepo: UserRepo = UserRepoImpl()) : ChatRepo {

    private val firestore = Firebase.firestore

    override suspend fun getChatFromChatID(chatID: String): Chat? =
        getChatDetailsRef(chatID)
            .get()
            .await()
            .toObject<Chat>()

    override fun getChatsForUser(userID: String) = callbackFlow<List<Chat>> {
        val listOfChatIDs = getPersonalizedChatsFirebaseRef(userID)
            .get()
            .await()
            .toObjects(ChatRepo.PersonalizedChat::class.java)
            .map { it.chatID }

        if (listOfChatIDs.isEmpty())
            trySend(emptyList())

        Timber.d("listOfChatIDs is $listOfChatIDs")

        if (listOfChatIDs.isNotEmpty()) {
            val chatsListener = firestore.collection(CHAT_DETAILS)
                .whereIn(Chat::chatID.name, listOfChatIDs)
                .orderBy(Chat::timeOfLastMessage.name, Query.Direction.DESCENDING)
                .addSnapshotListener { value, error ->
                    val chats = value?.toObjects(Chat::class.java)
                    Timber.e(error)
                    Timber.d("chats in addSnapshotListener() are $chats")

                    trySend(chats ?: listOf())
                }


            awaitClose {
                chatsListener.remove()
            }
        }

        awaitClose {

        }
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
        // PERSONALIZED_CHATS >> FILLER >> uid1234  >> chat1234
        getPersonalizedChatsFirebaseRef(Firebase.auth.uid!!)
            .document(chatID)
            .set(ChatRepo.PersonalizedChat(chatID))


        // Add to other individual list of chats
        // PERSONALIZED_CHATS >> FILLER >> uid0000 >> chat1234
        getPersonalizedChatsFirebaseRef(newContact.uid)
            .document(chatID)
            .set(ChatRepo.PersonalizedChat(chatID))

        return chatID
    }


    // chats >> chat1234 >> messages >> messageID
    override suspend fun sendMessage(chatID: String, message: Message): Boolean {
        return try {
            getMessagesCollectionRef(chatID)
                .document(message.messageID)
                .set(message)
                .addOnCompleteListener {
                    Timber.d("firestore.collection(CHATS_COLLECTION) is ${it.isSuccessful}")
                }
                .await()

            getMessagesCollectionRef(chatID)
                .document(message.messageID)
                .update(Message::messageStatus.name, MessageStatus.SENT)
                .addOnCompleteListener {
                    Timber.d("update(Message::messageStatus.name) is ${it.isSuccessful}")
                }
                .await()


            val chat = getChatFromChatID(chatID)

            getChatDetailsRef(chatID).update(
                mapOf(
                    Chat::lastMessage.name to message.message,
                    Chat::lastMessageSender.name to message.senderID,
                    Chat::timeOfLastMessage.name to message.timeSent,
                    Chat::unreadMessagesCount.name to (chat?.unreadMessagesCount ?: 0) + 1
                )
            )

            true
        } catch (e: Exception) {
            false
        }
    }


    // chats >> chat1234 >> messages
    override suspend fun getMessagesFromChatID(chatID: String) = callbackFlow<List<Message>> {
        val messagesSnapshotListener = getMessagesCollectionRef(chatID)
            .orderBy(Message::timeSent.name, Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                val messages = value?.toObjects(Message::class.java)
                Timber.d("messages is $messages")
                Timber.e(error)

                launch {
                    markUnreadMessagesAsRead(chatID, messages)
                }

                trySend(messages ?: listOf())
            }

        awaitClose {
            messagesSnapshotListener.remove()
        }
    }

    /**
     * Receives a list of messages, filters for the unread ones sent by the OTHER person and marks them as OPENED
     */
    private suspend fun markUnreadMessagesAsRead(chatID: String, messages: List<Message>?) =
        withContext(Dispatchers.IO) {
            val unreadMessages = messages?.filter {
                (it.messageStatus == MessageStatus.SENT)
                        &&
                        it.senderID != Firebase.auth.uid
            }
            Timber.d("unreadMessages are $unreadMessages")

            if (!unreadMessages.isNullOrEmpty()) {
                val messagesCollection = getMessagesCollectionRef(chatID)
                Timber.d("unreadMessages updated")

                unreadMessages.forEach {
                    messagesCollection.document(it.messageID)
                        .update(Message::messageStatus.name, MessageStatus.OPENED)
                }
            }
        }


    override suspend fun resetUnreadMessagesCount(chatID: String) {
        val chat = getChatFromChatID(chatID)

        if (chat?.lastMessageSender != Firebase.auth.uid)
            getChatDetailsRef(chatID).update(
                mapOf(
                    Chat::unreadMessagesCount.name to 0,
                    Chat::lastMessageStatus.name to MessageStatus.OPENED
                )
            )
    }


    override suspend fun unsendMessage(chatID: String, messageID: String): Boolean {
        val chatMessagesCollection = getMessagesCollectionRef(chatID)
        val lastTwoMessages =
            chatMessagesCollection.orderBy(Message::timeSent.name, Query.Direction.DESCENDING)
                .limit(2).get()
                .await().toObjects(Message::class.java)

        val lastMessageID = lastTwoMessages.firstOrNull()?.messageID

        chatMessagesCollection.document(messageID).delete()

        // If the message being unsent is not the latest message, we don't have to update ChatDetails
        if (lastMessageID != messageID)
            return true

        // If only one message has ever been sent
        val newLastMessage = if (lastTwoMessages.size == 1) null else lastTwoMessages.lastOrNull()

        val chatDetailsRef = getChatDetailsRef(chatID)
        val chat = chatDetailsRef.get().await().toObject<Chat>()
        val newUnreadMessageCount = (chat?.unreadMessagesCount?.minus(1))?.coerceAtLeast(0) ?: 0

        return chatDetailsRef.update(
            mapOf(
                Chat::lastMessage.name to newLastMessage?.message,
                Chat::lastMessageSender.name to newLastMessage?.senderID,
                Chat::timeOfLastMessage.name to newLastMessage?.timeSent,
                Chat::unreadMessagesCount.name to newUnreadMessageCount,
                Chat::lastMessageStatus.name to newLastMessage?.messageStatus
            )
        )
            .isSuccessful
    }

    override suspend fun updateMessageStatus(messageID: String, messageStatus: MessageStatus) {

    }
}