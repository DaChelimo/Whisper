package com.da_chelimo.whisper.chats.repo.unread_messages.models

import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.core.domain.MiniUser

data class UnreadMessages(
    val chatID: String,
    val currentMiniUser: MiniUser?,
    val otherMiniUser: MiniUser?,
    val listOfMessages: List<Message>
)
