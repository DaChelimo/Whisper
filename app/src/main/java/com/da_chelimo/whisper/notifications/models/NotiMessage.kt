package com.da_chelimo.whisper.notifications.models

import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.toMessageType

data class NotiMessage(
    val chatID: String,
    val senderUID: String,
    val messageID: String,
    val messageContent: String,
    val timeSent: Long
)


fun Message.toNotiMessage(chatID: String) =
    NotiMessage(
        chatID = chatID,
        senderUID = senderID,
        messageID = messageID,
        messageContent = messageType.toMessageType().message,
        timeSent = timeSent
    )
