package com.da_chelimo.whisper.chats.presentation.select_contact.screens

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.chats.domain.ContactState
import com.da_chelimo.whisper.chats.repo.contacts.ContactsRepo
import com.da_chelimo.whisper.core.domain.User
import com.da_chelimo.whisper.core.presentation.ui.ActualChat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class SelectContactsViewModel(
    private val contactsRepo: ContactsRepo
) : ViewModel() {

    val contactsOnWhisper = contactsRepo.contactsOnWhisper.map { contacts ->
        if (!contacts.isNullOrEmpty()) ContactState.Success(contacts)
//        else if (contacts.isEmpty()) ContactState.Empty TODO: How to return this
        else ContactState.Fetching
    }
    val inviteToWhisperList = mutableStateListOf<User>()

    private val _shouldNavigateToActualChat = MutableStateFlow<ActualChat?>(null)
    val shouldNavigateToActualChat: StateFlow<ActualChat?> = _shouldNavigateToActualChat

    /**
     * If the user selects someone they are already talking with, return them to their ongoing coveration
     * { in either scenario, make sure the NavStack is popped to the start }
     */
    fun startOrResumeConversation(newContact: User) = viewModelScope.launch {
        val existingChatID = contactsRepo.checkForPreExistingChat(newContact)
        Timber.d("startOrResumeConversation.existingChatID is $existingChatID")

        Timber.d("existingChatID is $existingChatID")
        Timber.d("newContact.uid is ${newContact.uid}")

        _shouldNavigateToActualChat.value = if (existingChatID != null)
            ActualChat(chatId = existingChatID, newContact = null)
        else
            ActualChat(chatId = null, newContact = newContact.uid)
    }

    suspend fun fetchContactsOnWhisper(context: Context) {
        contactsRepo.refreshContactsOnWhisper(context)
    }


    fun resetShouldNavigateToActualChat() {
        _shouldNavigateToActualChat.value = null
    }
}