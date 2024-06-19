package com.da_chelimo.whisper.chats.presentation.actual_chat.screens.send_image

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.domain.MessageType
import com.da_chelimo.whisper.chats.repo.chats.ChatRepoImpl
import com.da_chelimo.whisper.chats.repo.messages.MessagesRepo
import com.da_chelimo.whisper.chats.repo.messages.MessagesRepoImpl
import com.da_chelimo.whisper.core.domain.TaskState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class SendImageViewModel(
    private val messagesRepo: MessagesRepo = MessagesRepoImpl(ChatRepoImpl())
): ViewModel() {

    private val _typedMessage = MutableStateFlow(TextFieldValue(""))
    val typedMessage: StateFlow<TextFieldValue> = _typedMessage

    private val _sendImageState = MutableStateFlow<TaskState>(TaskState.NONE)
    val sendImageState: StateFlow<TaskState> = _sendImageState

    fun updateMessage(newMessage: TextFieldValue) {
        _typedMessage.value = newMessage
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun sendMessage(chatID: String, imageUri: String) = viewModelScope.launch {
        if (sendImageState.value is TaskState.LOADING) return@launch

        _sendImageState.value = TaskState.LOADING()

        val message = Message(
            senderID = Firebase.auth.uid!!,
            messageID = UUID.randomUUID().toString(),
            message = typedMessage.value.text,
            messageImage = imageUri,
            timeSent = System.currentTimeMillis(),
            messageStatus = MessageStatus.SENT,
            messageType = MessageType.Image
        )

        GlobalScope.launch {
            messagesRepo.sendMessage(chatID, message)
        }

        delay(1000)
        _sendImageState.value = TaskState.DONE.SUCCESS
    }


    fun resetSendState() {
        _sendImageState.value = TaskState.NONE
    }
}