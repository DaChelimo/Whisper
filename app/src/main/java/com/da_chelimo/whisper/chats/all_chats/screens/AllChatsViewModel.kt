package com.da_chelimo.whisper.chats.all_chats.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo
import com.da_chelimo.whisper.chats.repo.chats.ChatRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import timber.log.Timber

class AllChatsViewModel(
    private val chatRepo: ChatRepo = ChatRepoImpl()
) : ViewModel() {

    val chats = mutableStateListOf<Chat>()

    init {
        viewModelScope.launch {
            loadChats()
        }
    }

    private suspend fun loadChats() {
        Timber.d("uid is ${Firebase.auth.uid}")
        Firebase.auth.uid?.let { uid ->
            val remoteChats = chatRepo.getChatsForUser(uid)
            Timber.d("remoteChats is $remoteChats for uid: $uid")
            chats.addAll(remoteChats)
        }
    }

}