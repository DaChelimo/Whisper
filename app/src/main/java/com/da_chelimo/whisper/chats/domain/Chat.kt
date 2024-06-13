package com.da_chelimo.whisper.chats.domain

import com.da_chelimo.whisper.core.domain.MiniUser

data class Chat(
    val chatID: String,

    val firstMiniUser: MiniUser,
    val secondMiniUser: MiniUser,

    var unreadMessagesCount: Int,

    var lastMessage: String,
    var lastMessageSender: String,
    val lastMessageStatus: MessageStatus,
    var timeOfLastMessage: Long // Time in millis when the last text was sent
) {

    constructor() : this(
        "",
        MiniUser("", "", null),
        MiniUser("", "", null),
        0,
        "",
        "",
        MessageStatus.SENT,
        0
    )


    companion object {
        val TEST_CHAT = Chat(
            "chat1234",
            MiniUser("Andrew", "1234", null),
            MiniUser("Diana", "0000", null),
            6,
            "Wanna come over for lunch?",
            "0000",
            MessageStatus.SENT,
            1234455
        )
    }
}


