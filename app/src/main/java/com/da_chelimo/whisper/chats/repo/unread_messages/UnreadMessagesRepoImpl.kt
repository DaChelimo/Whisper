package com.da_chelimo.whisper.chats.repo.unread_messages

import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.getChatDetailsRef
import com.da_chelimo.whisper.chats.repo.chats.ChatRepoImpl
import com.da_chelimo.whisper.chats.repo.unread_messages.models.UnreadMessages
import com.da_chelimo.whisper.core.domain.toMiniUser
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.tasks.await
import timber.log.Timber


class UnreadMessagesRepoImpl(
    private val userRepo: UserRepo = UserRepoImpl(),
    private val chatRepo: ChatRepo = ChatRepoImpl()
) : UnreadMessagesRepo {

    private val firestore = Firebase.firestore


    override fun getUnreadChatIDS(): Flow<List<String>> = callbackFlow {
        /**
         * Make the last seen 0 since... in the Chat collection, we aren't
         * updating the last seen time (to reduce the number of calls)
         * {SKETCHYYYYY... I knowwwww.... And I'm... I'm.... I'm SORRYYYYY :(}
         */
        val currentUser = Firebase.auth.uid?.let {
            userRepo.getUserFromUID(it)?.toMiniUser()
        }
        Timber.d("currentUser is $currentUser")

        val firstUserFilter = Filter.equalTo(Chat::firstMiniUser.name, currentUser)
        val secondUserFilter = Filter.equalTo(Chat::secondMiniUser.name, currentUser)


        val chatIDSListener = firestore.collection(ChatRepo.CHAT_DETAILS)
            .where((Filter.or(firstUserFilter, secondUserFilter)))
            .whereGreaterThan(Chat::unreadMessagesCount.name, 0)
            .addSnapshotListener { value, error ->
//                error?.printStackTrace()

                val chatIDs = value?.toObjects(Chat::class.java)?.map { it.chatID }
                Timber.d("chatIDs is $chatIDs")

                if (chatIDs != null) {
                    trySend(chatIDs)
                }
            }

        awaitClose {
            chatIDSListener.remove()
        }
    }


    override fun getUnreadMessages(): Flow<List<UnreadMessages>> = callbackFlow {
        val currentUser = Firebase.auth.uid?.let {
            userRepo.getUserFromUID(it)?.toMiniUser()
        }
        val firstUserFilter = Filter.equalTo(Chat::firstMiniUser.name, currentUser)
        val secondUserFilter = Filter.equalTo(Chat::secondMiniUser.name, currentUser)

        Timber.d("Im callbackFlow -> getUnreadMessages() ")

        val IDS = firestore.collection(ChatRepo.CHAT_DETAILS)
            .where((Filter.or(firstUserFilter, secondUserFilter)))
            .whereGreaterThan(Chat::unreadMessagesCount.name, 0)
            .get()
            .await()
            .toObjects(Chat::class.java)
            .map { it.chatID }

        Timber.d("IDS in getUnreadMessages() is $IDS")


        getUnreadChatIDS().collectLatest { listOfChatIDS ->
            val listOfUnreadMessages = listOfChatIDS.map { chatID ->
                val chat = chatRepo.getChatFromChatID(chatID)

                val messages = ChatRepo.getMessagesCollectionRef(chatID)
                    .whereNotEqualTo(Message::messageStatus.name, MessageStatus.OPENED)
                    .orderBy(Message::timeSent.name, Query.Direction.ASCENDING)
                    .get()
                    .await()
                    .toObjects(Message::class.java)

                updateMessagesAsReceived(chatID, messages)

                val firstUserIsCurrentUser = chat?.firstMiniUser?.uid == Firebase.auth.uid
                val currentMiniUser =
                    if (firstUserIsCurrentUser) chat?.firstMiniUser else chat?.secondMiniUser
                val otherMiniUser =
                    if (firstUserIsCurrentUser) chat?.secondMiniUser else chat?.firstMiniUser

                UnreadMessages(chatID, currentMiniUser, otherMiniUser, messages)
            }

            Timber.d("listOfUnreadMessages is $listOfUnreadMessages")


            trySend(listOfUnreadMessages)
        }

        awaitClose {}
    }


    override fun updateMessagesAsReceived(chatID: String, messages: List<Message>) {
        getChatDetailsRef(chatID)
            .update(Chat::lastMessageStatus.name, MessageStatus.RECEIVED)

        val messagesRef = ChatRepo.getMessagesCollectionRef(chatID)
        messages.forEach {
            messagesRef.document(it.messageID)
                .update(Message::messageStatus.name, MessageStatus.RECEIVED)
        }
    }


    override suspend fun updateMessagesAsOpened(chatID: String?) {
        /**
         * Mark all messages as opened if:
         * 1) it was not opened before AND
         * 2) it is from the other person
         */
        val messageIDS = ChatRepo.getMessagesCollectionRef(chatID ?: return)
            .whereIn(
                Message::messageStatus.name,
                listOf(MessageStatus.NOT_SENT, MessageStatus.SENT, MessageStatus.RECEIVED)
            )
            .whereNotEqualTo(Message::senderID.name, Firebase.auth.uid)
            .get()
            .await()
            .toObjects<Message>()
            .map { it.messageID }

        messageIDS.forEach { messageId ->
            ChatRepo.getMessagesCollectionRef(chatID)
                .document(messageId)
                .update(Message::messageStatus.name, MessageStatus.OPENED)
        }
    }


    override suspend fun resetUnreadMessagesCount(chatID: String) {
        val chat = chatRepo.getChatFromChatID(chatID)
        val lastMessageSender = chat?.lastMessageSender
        val myUID = Firebase.auth.uid
        val isOtherUser = myUID != lastMessageSender

        Timber.d("isOtherUser is $isOtherUser")

        if (isOtherUser)
            getChatDetailsRef(chatID).update(
                mapOf(
                    Chat::unreadMessagesCount.name to 0,
                    Chat::lastMessageStatus.name to MessageStatus.OPENED
                )
            )
    }

}