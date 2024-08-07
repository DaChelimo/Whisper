package com.da_chelimo.whisper.chats.repo.chats

import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.CHAT_DETAILS
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.getChatDetailsRef
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.getPersonalizedChatsFirebaseRef
import com.da_chelimo.whisper.core.domain.MiniUser
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.UUID

class ChatRepoImpl(private val userRepo: UserRepo = UserRepoImpl()) : ChatRepo {

    private val firestore = Firebase.firestore


    override suspend fun getChatFromChatID(chatID: String): Chat? =
        getChatDetailsRef(chatID)
            .get()
            .await()
            .toObject<Chat>()


    // In case the user gets a new chat while in the AllChatsScreen
    override fun getChatIdsForUser(userID: String) = callbackFlow {
        val chatIdListener = getPersonalizedChatsFirebaseRef(userID)
            .addSnapshotListener { value, _ ->
                val listOfChatIDs =
                    value?.toObjects(ChatRepo.PersonalizedChat::class.java)?.map { it.chatID }
                        ?: listOf()

                trySend(listOfChatIDs)
            }

        awaitClose {
            chatIdListener.remove()
        }
    }

    override fun getChatsForUser(userID: String?) = callbackFlow<List<Chat>?> {
        var chatListener: ListenerRegistration? = null

        if (userID != null) {

            getChatIdsForUser(userID).collectLatest { listOfChatIDs ->
                if (listOfChatIDs.isEmpty())
                    trySend(listOf())

                Timber.d("listOfChatIDs is $listOfChatIDs")

                if (listOfChatIDs.isNotEmpty()) {
                    chatListener = firestore.collection(CHAT_DETAILS)
                        .whereIn(Chat::chatID.name, listOfChatIDs)
                        .orderBy(Chat::timeOfLastMessage.name, Query.Direction.DESCENDING)
                        .addSnapshotListener { value, error ->
                            try {
                                val chats = value?.toObjects(Chat::class.java)
                                Timber.e(error)
                                Timber.d("chats in addSnapshotListener() are $chats")

                                trySend(chats ?: listOf())
                            } catch (e: Exception) {
                                Timber.e(e)
                            }
                        }


//                awaitClose {
//                    chatsListener.remove()
//                }
                }
            }
        }

        awaitClose {
            chatListener?.remove()
        }
    }

    override suspend fun createConversation(newContact: MiniUser): String {
        val chatID = UUID.randomUUID().toString()
        val currentUser = userRepo.getUserFromUID(Firebase.auth.uid!!)
        val newChat = Chat.generateNewChatUsingCurrentUser(chatID, currentUser, newContact)

        //  chat_details >> chat1234
        firestore.collection(CHAT_DETAILS)
            .document(chatID)
            .set(newChat)


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

    override suspend fun disableChatsForUser(userID: String) {
        val chatIDs = getPersonalizedChatsFirebaseRef(userID)
            .get().await().toObjects(ChatRepo.PersonalizedChat::class.java)
            .map { it.chatID }
        Timber.d("chatIDs to disable are: $chatIDs")

        chatIDs.forEach { chatID -> disableChat(chatID) }

        // Remove the list of personalized chats he/she used to have {prevents space wastage}
        val listOfPersonalizedIDs = getPersonalizedChatsFirebaseRef(userID).get()
            .await().toObjects(ChatRepo.PersonalizedChat::class.java).map { it.chatID }

        listOfPersonalizedIDs.forEach { chatID ->
            getPersonalizedChatsFirebaseRef(userID).document(chatID).delete()
        }
    }


    override fun disableChat(chatID: String) {
        val disableChat = getChatDetailsRef(chatID)
            .update(Chat::isDisabled.name, true)
            .isSuccessful

        Timber.d("disableChat.isSuccessful is $disableChat")
    }


    override suspend fun updateMessageStatus(messageID: String, messageStatus: MessageStatus) {

    }
}