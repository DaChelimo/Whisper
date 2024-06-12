package com.da_chelimo.whisper.chats.presentation.all_chats.screens

import androidx.lifecycle.ViewModel
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo
import com.da_chelimo.whisper.chats.repo.chats.ChatRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AllChatsViewModel(
    private val chatRepo: ChatRepo = ChatRepoImpl()
) : ViewModel() {

    val chats = chatRepo.getChatsForUser(Firebase.auth.uid!!)

//    init {
//        viewModelScope.launch {
//            loadChats()
//        }
//    }
//
//    private suspend fun loadChats() {
//        Timber.d("uid is ${Firebase.auth.uid}")
//        Firebase.auth.uid?.let { uid ->
//            val remoteChats = chatRepo.getChatsForUser(uid)
//            Timber.d("remoteChats is $remoteChats for uid: $uid")
//            chats.addAll(remoteChats)
//        }
//    }

}