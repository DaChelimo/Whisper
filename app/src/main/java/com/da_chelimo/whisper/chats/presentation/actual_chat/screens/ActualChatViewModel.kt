package com.da_chelimo.whisper.chats.presentation.actual_chat.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.presentation.utils.toActualChatSeparatorTime
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo
import com.da_chelimo.whisper.chats.repo.chats.ChatRepoImpl
import com.da_chelimo.whisper.chats.repo.contacts.ContactsRepo
import com.da_chelimo.whisper.chats.repo.messages.MessagesRepo
import com.da_chelimo.whisper.chats.repo.messages.MessagesRepoImpl
import com.da_chelimo.whisper.core.domain.MiniUser
import com.da_chelimo.whisper.core.domain.toMiniUser
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
    private val chatRepo: ChatRepo = ChatRepoImpl(userRepo),
    private val messagesRepo: MessagesRepo = MessagesRepoImpl(chatRepo),
    private val contactsRepo: ContactsRepo
) : ViewModel() {

    private val _textMessage = MutableStateFlow(TextFieldValue(""))
    val textMessage: StateFlow<TextFieldValue> = _textMessage

    var chatID: String? = null
        private set


    val chat = MutableStateFlow<Chat?>(null)
    val messages = mutableStateListOf<Message>()

    val mapOfMessageIDAndDateInString = mutableMapOf<String, String>()


    private val _otherUser = MutableStateFlow<MiniUser?>(null)
    val otherUser: StateFlow<MiniUser?> = _otherUser

    private val _openMediaPicker = MutableStateFlow(false)
    val openMediaPicker: StateFlow<Boolean> = _openMediaPicker

    private val _isEditing = MutableStateFlow<String?>(null)
    val isEditing: StateFlow<String?> = _isEditing

    val doesOtherUserAccountExist = MutableStateFlow(true)

    suspend fun loadChat(chatID: String?) {
        if (chatID != null) {
            chat.value = chatRepo.getChatFromChatID(chatID)
            Timber.d("chat.value is ${chat.value}")
            Timber.d("chat.value?.isDisabled is ${chat.value?.isDisabled}")
        }
    }

    /**
     * Gets the other user's profile either using the chatID (existing chat) or newContact (new chat)
     */
    suspend fun loadOtherUser(chatID: String?, newContactUID: String?) {
        Timber.d("chatID in loadOtherUser is $chatID")
        _otherUser.value =
            if (chatID == null) {
                contactsRepo.getContactFromUID(newContactUID!!)?.toMiniUser()
            } else {
                val chat = chat.value

                if (chat?.firstMiniUser?.uid == Firebase.auth.uid) chat?.secondMiniUser
                else chat?.firstMiniUser
            }

        Timber.d("otherUser.value is ${otherUser.value}")
        val otherUserID = otherUser.value?.uid
        doesOtherUserAccountExist.value = otherUserID?.let { userRepo.getUserFromUID(it) } != null
    }

    /**
     * Fetch chats from a given chatID
     *
     * @param paramChatID ~ if chatID == null, it means this is a new conversation being created.
     *                  Therefore, we need to generate a chatID and update it on the personalized chats in Fire DB
     *                  if chatID != null, this is an existing conversation, so you can fetch the messages.
     */
    fun fetchChats(paramChatID: String?) = viewModelScope.launch {
        chatID = paramChatID

        if (chatID != null) {
            Timber.d("chatID is $chatID")

            messagesRepo.getMessagesFromChatID(chatID!!)
                .onEach { // TODO: Improve this coz this is TERRIBLEEEEEE :)
                    Timber.d("chatRepo.getMessagesFromChatID(chatID!!).collect is $it")
                    it.reversed().forEach { message ->
                        val timeTitle =
                            message.timeSent.toActualChatSeparatorTime() ?: return@forEach
                        val isDateInMap =
                            mapOfMessageIDAndDateInString.values.contains(timeTitle)

                        if (!isDateInMap)
                            mapOfMessageIDAndDateInString[message.messageID] =
                                timeTitle
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

        if (chat.value?.isDisabled == true) { // Chat disabled; do not send & clear message bar
            _textMessage.value = TextFieldValue("")
        } else {
            val message = Message(
                senderID = Firebase.auth.uid!!,
                messageID = UUID.randomUUID().toString(),
                message = textMessage.value.text,
                timeSent = System.currentTimeMillis(),
                messageStatus = MessageStatus.NOT_SENT
            )

            _textMessage.value = TextFieldValue("")
            messagesRepo.sendMessage(chatID!!, message)
        }
    }

    fun launchMessageEditing(messageID: String, oldMessage: String) {
        _isEditing.value = messageID
        updateComposeMessage(
            TextFieldValue(
                oldMessage,
                selection = TextRange(index = oldMessage.length)
            )
        )
    }

    private suspend fun editMessage() {
        val editedMessageID = isEditing.value!!
        val editedMessage = textMessage.value.text

        _isEditing.value = null
        _textMessage.value = TextFieldValue("")

        messagesRepo.editMessage(
            chatID = chatID!!,
            messageID = editedMessageID,
            newMessage = editedMessage
        )
    }

    fun unsendMessage(messageID: String) = viewModelScope.launch {
        messagesRepo.unsendMessage(chatID!!, messageID)
    }

    fun updateComposeMessage(newMessage: TextFieldValue) {
        _textMessage.value = newMessage
    }

    fun updateOpenMediaPicker(shouldOpen: Boolean) {
        _openMediaPicker.value = shouldOpen
    }


    fun resetUnreadMessagesCountOnChatExit() = viewModelScope.launch{
        Timber.d("resetUnreadMessagesCountOnChatExit with chatID as $chatID")
        chatID?.let { chatRepo.resetUnreadMessagesCount(it) }
    }
}