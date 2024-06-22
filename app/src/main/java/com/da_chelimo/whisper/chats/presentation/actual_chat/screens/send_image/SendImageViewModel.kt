package com.da_chelimo.whisper.chats.presentation.actual_chat.screens.send_image

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.chats.repo.chats.ChatRepoImpl
import com.da_chelimo.whisper.chats.repo.messages.MessagesRepo
import com.da_chelimo.whisper.chats.repo.messages.MessagesRepoImpl
import com.da_chelimo.whisper.core.domain.TaskState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

        GlobalScope.launch {
            messagesRepo.sendImageMessage(chatID, imageUri, typedMessage.value.text)
        }

        delay(1000)
        _sendImageState.value = TaskState.DONE.SUCCESS
    }


    fun resetSendState() {
        _sendImageState.value = TaskState.NONE
    }
}