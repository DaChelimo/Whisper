package com.da_chelimo.whisper.chats.repo.contacts.local

import com.da_chelimo.whisper.chats.repo.contacts.local.entity.LocalContact
import com.da_chelimo.whisper.core.domain.User

fun List<User>.toLocalContacts() =
    map { it.toLocalContacts() }
fun List<LocalContact>.toUser() =
    map { it.toUser() }


fun User.toLocalContacts() =
    LocalContact(this)

fun LocalContact.toUser() = this.contact