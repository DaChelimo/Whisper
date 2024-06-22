package com.da_chelimo.whisper.chats.repo.messages

import androidx.core.net.toUri
import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.domain.MessageType
import com.da_chelimo.whisper.chats.domain.toMessageType
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.UUID


class MessagesRepoImpl(
    private val chatRepo: ChatRepo
) : MessagesRepo {

    override suspend fun sendImageMessage(
        chatID: String,
        imageUri: String,
        messageText: String?
    ): Boolean {
        val imageUrl = uploadFileUsingUri("$chatID/IMAGES/${UUID.randomUUID()}", imageUri)
        val imageType = MessageType.Image(messageText ?: "", imageUrl)

        return sendMessage(chatID, imageType)
    }

    override suspend fun sendAudioMessage(
        chatID: String,
        audioUri: String,
        duration: Long
    ): Boolean {
        val audioUrl = uploadFileUsingUri("$chatID/AUDIO/${UUID.randomUUID()}", audioUri)
        val audioType= MessageType.Audio(duration, audioUrl)

        return sendMessage(chatID, audioType)
    }

    // chats >> chat1234 >> messages >> messageID
    override suspend fun sendMessage(
        chatID: String,
        messageType: MessageType
    ): Boolean {
        val message = Message(
            senderID = Firebase.auth.uid!!,
            messageID = UUID.randomUUID().toString(),
            messageType = messageType.toFirebaseMap(),
            timeSent = System.currentTimeMillis(),
            messageStatus = MessageStatus.NOT_SENT
        )


        return try {
            ChatRepo.getMessagesCollectionRef(chatID)
                .document(message.messageID)
                .set(message)
                .addOnCompleteListener {
                    Timber.d("firestore.collection(CHATS_COLLECTION) is ${it.isSuccessful}")
                }
                .await()

            ChatRepo.getMessagesCollectionRef(chatID)
                .document(message.messageID)
                .update(Message::messageStatus.name, MessageStatus.SENT)
                .addOnCompleteListener {
                    Timber.d("update(Message::messageStatus.name) is ${it.isSuccessful}")
                }
                .await()


            val chat = chatRepo.getChatFromChatID(chatID)

            ChatRepo.getChatDetailsRef(chatID).update(
                mapOf(
                    Chat::lastMessageSender.name to message.senderID,
                    Chat::timeOfLastMessage.name to message.timeSent,
                    Chat::lastMessageType.name to message.messageType,
                    Chat::lastMessageStatus.name to MessageStatus.SENT,
                    Chat::unreadMessagesCount.name to (chat?.unreadMessagesCount ?: 0) + 1
                )
            )

            true
        } catch (e: Exception) {
            false
        }
    }


    /**
     * Returns the download url of the file that has been uploaded to Firebase Storage
     */
    private suspend fun uploadFileUsingUri(storageRef: String, fileUri: String): String =
        Firebase.storage.getReference(storageRef)
            .putFile(fileUri.toUri())
            .await().storage
            .downloadUrl.await().toString()


    // chats >> chat1234 >> messages
    override suspend fun getMessagesFromChatID(chatID: String) = callbackFlow<List<Message>> {
        val messagesSnapshotListener = ChatRepo.getMessagesCollectionRef(chatID)
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
                val messagesCollection = ChatRepo.getMessagesCollectionRef(chatID)
                Timber.d("unreadMessages updated")

                unreadMessages.forEach {
                    messagesCollection.document(it.messageID)
                        .update(Message::messageStatus.name, MessageStatus.OPENED)
                }
            }
        }


    override suspend fun editMessage(chatID: String, messageID: String, newMessage: String) {
        val chatMessagesCollection = ChatRepo.getMessagesCollectionRef(chatID)

        // Edit the message in the main chats collection
        val messageRef = chatMessagesCollection.document(messageID)
        val newMessageType = messageRef.get().await()
            .toObject<Message>()?.messageType?.toMessageType()?.apply { message = newMessage }
            ?.toFirebaseMap()

        messageRef.update(
            mapOf(
                Message::messageType.name to newMessageType,
                Message::wasEdited.name to true
            )
        )


        // Checks if the message being edited is the last message
        // If yes, update the last message in chat details
        val lastMessage =
            chatMessagesCollection.orderBy(Message::timeSent.name, Query.Direction.DESCENDING)
                .limit(1).get().await().toObjects(Message::class.java).firstOrNull()

        val lastMessageID = lastMessage?.messageID
        val isLastMessage = lastMessageID == messageID

        if (isLastMessage) {
            ChatRepo.getChatDetailsRef(chatID)
                .update(
                    Chat::lastMessageType.name, newMessageType
                )
        }
    }


    override suspend fun unsendMessage(chatID: String, messageID: String): Boolean {
        val chatMessagesCollection = ChatRepo.getMessagesCollectionRef(chatID)
        val lastTwoMessages =
            chatMessagesCollection.orderBy(Message::timeSent.name, Query.Direction.DESCENDING)
                .limit(2).get()
                .await().toObjects(Message::class.java)

        val lastMessageID = lastTwoMessages.firstOrNull()?.messageID

        // Deletes the message
        chatMessagesCollection.document(messageID).delete()

        // If the message being unsent is not the latest message, we don't have to update ChatDetails
        if (lastMessageID != messageID)
            return true

        // If only one message has ever been sent
        val newLastMessage = if (lastTwoMessages.size == 1) null else lastTwoMessages.lastOrNull()

        val chatDetailsRef = ChatRepo.getChatDetailsRef(chatID)
        val chat = chatDetailsRef.get().await().toObject<Chat>()
        val newUnreadMessageCount = (chat?.unreadMessagesCount?.minus(1))?.coerceAtLeast(0) ?: 0

        return chatDetailsRef.update(
            mapOf(
                Chat::lastMessageType.name to newLastMessage?.messageType,
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