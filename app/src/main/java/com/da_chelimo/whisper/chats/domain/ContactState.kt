package com.da_chelimo.whisper.chats.domain

import com.da_chelimo.whisper.core.domain.User

sealed class ContactState {
    data object Fetching: ContactState()
    data object Empty: ContactState()

    class Success(val listOfContacts: List<User>): ContactState()
}