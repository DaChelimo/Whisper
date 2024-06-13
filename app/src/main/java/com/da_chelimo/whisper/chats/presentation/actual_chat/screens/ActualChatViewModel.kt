package com.da_chelimo.whisper.chats.presentation.actual_chat.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.presentation.utils.toActualChatSeparatorTime
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo
import com.da_chelimo.whisper.chats.repo.chats.ChatRepoImpl
import com.da_chelimo.whisper.chats.repo.contacts.ContactsRepo
import com.da_chelimo.whisper.core.domain.User
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID

class ActualChatViewModel(
    private val userRepo: UserRepo = UserRepoImpl(),
    private val chatRepo: ChatRepo = ChatRepoImpl(),
    private val contactsRepo: ContactsRepo
) : ViewModel() {


    private val _textMessage = MutableStateFlow(TextFieldValue(""))
    val textMessage: StateFlow<TextFieldValue> = _textMessage

    private var chatID: String? = null
    private var newContact: String? = null

    val messages = mutableStateListOf<Message>()

    val mapOfMessageIDAndDateInString = mutableMapOf<String, String>()

    private val _otherUser = MutableStateFlow<User?>(null)
    val otherUser: StateFlow<User?> = _otherUser


    private val _isEditing = MutableStateFlow<String?>(null)
    val isEditing: StateFlow<String?> = _isEditing


    /**
     * Gets the other user's profile either using the chatID (existing chat) or newContact (new chat)
     */
    suspend fun loadOtherUser(chatID: String?, newContactUID: String?) {
        _otherUser.value =
            if (chatID == null)
                contactsRepo.getContactFromUID(newContactUID!!)
            else {
                val chat = chatRepo.getChatFromChatID(chatID)
                val otherUserUID =
                    if (chat?.firstMiniUser?.uid == Firebase.auth.uid) chat?.secondMiniUser
                    else chat?.firstMiniUser

                val remoteUser = userRepo.getUserFromUID(otherUserUID?.uid!!)
                Timber.d("remoteUser is $remoteUser")
                remoteUser
            }
        Timber.d("otherUser.value is ${otherUser.value}")
        Timber.d("newContact is $newContact")
    }

    /**
     * Fetch chats from a given chatID
     *
     * @param paramChatID ~ if chatID == null, it means this is a new conversation being created.
     *                  Therefore, we need to generate a chatID and update it on the personalized chats in Fire DB
     *                  if chatID != null, this is an existing conversation, so you can fetch the messages.
     */
    suspend fun fetchChats(paramChatID: String?) {
        chatID = paramChatID

        if (chatID != null) {
            Timber.d("chatID is $chatID")

            chatRepo.getMessagesFromChatID(chatID!!)
                .onEach { // TODO: Improve this coz this is TERRIBLEEEEEE :)
                    Timber.d("chatRepo.getMessagesFromChatID(chatID!!).collect is $it")
                    it.reversed().forEach { message ->
                        val isDateInMap =
                            mapOfMessageIDAndDateInString.values.contains(message.timeSent.toActualChatSeparatorTime())

                        if (!isDateInMap)
                            mapOfMessageIDAndDateInString[message.messageID] =
                                message.timeSent.toActualChatSeparatorTime()
                    }

                    messages.clear()
                    messages.addAll(it)
                }
                .launchIn(viewModelScope)

            /**
             * If chats have been opened by the user who had unread messages,
             * reset the unreadMessagesCount to 0
             */
            chatRepo.resetUnreadMessagesCount(chatID!!)
        }
    }

    private suspend fun createConversation() {
        Timber.d("otherUser.value in createConversation() is ${otherUser.value}")
        otherUser.value?.let {
            chatID = chatRepo.createConversation(it)
            fetchChats(chatID)
        }
    }


    fun sendOrEditMessage() = viewModelScope.launch {
        if (textMessage.value.text.isBlank()) // You can't send an empty message
            return@launch


        if (isEditing.value != null)
            editMessage()
        else
            sendMessage()
    }

    private suspend fun sendMessage() {
        if (chatID == null)            // If it's a new person, create a new chat
            createConversation()

        val message = Message(
            senderID = Firebase.auth.uid!!,
            messageID = UUID.randomUUID().toString(),
            message = textMessage.value.text,
            timeSent = System.currentTimeMillis(),
            messageStatus = MessageStatus.NOT_SENT
        )

        _textMessage.value = TextFieldValue("")
        chatRepo.sendMessage(chatID!!, message)
    }

    fun launchMessageEditing(messageID: String, oldMessage: String) {
        _isEditing.value = messageID
        updateComposeMessage(TextFieldValue(oldMessage, selection = TextRange(index = oldMessage.length)))
    }

    private suspend fun editMessage() {
        val editedMessageID = isEditing.value!!
        val editedMessage = textMessage.value.text

        _isEditing.value = null
        _textMessage.value = TextFieldValue("")

        chatRepo.editMessage(
            chatID = chatID!!,
            messageID = editedMessageID,
            newMessage = editedMessage
        )
    }

    fun unsendMessage(messageID: String) = viewModelScope.launch {
        chatRepo.unsendMessage(chatID!!, messageID)
    }

    fun updateComposeMessage(newMessage: TextFieldValue) {
        _textMessage.value = newMessage
    }

}