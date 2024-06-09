package com.da_chelimo.whisper.chats.start_chat.screens

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.da_chelimo.whisper.chats.repo.contacts.ContactsRepo
import com.da_chelimo.whisper.core.domain.User

class SelectContactsViewModel(
    private val contactsRepo: ContactsRepo
) : ViewModel() {

    val contactsOnWhisper = contactsRepo.contactsOnWhisper
    val inviteToWhisperList = mutableStateListOf<User>()

    suspend fun fetchContactsOnWhisper(context: Context) {
        contactsRepo.getContactsOnWhisper(context)
    }
}