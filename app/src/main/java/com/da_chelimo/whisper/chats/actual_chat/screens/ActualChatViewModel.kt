package com.da_chelimo.whisper.chats.actual_chat.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ActualChatViewModel: ViewModel() {

    private val _composeMessage = MutableStateFlow("")
    val composeMessage: StateFlow<String> = _composeMessage



    fun sendMessage() {

    }


    fun updateComposeMessage(newMessage: String) {
        _composeMessage.value = newMessage
    }

}