package com.da_chelimo.whisper.chats.domain

import com.da_chelimo.whisper.core.domain.MiniUser
import java.util.UUID

data class Chat(
    val chatID: String,

    val firstMiniUser: MiniUser,
    val secondMiniUser: MiniUser,

    var unreadMessagesCount: Int,

    var lastMessage: String?,
    var lastMessageSender: String?,
    val lastMessageStatus: MessageStatus?,
    var timeOfLastMessage: Long?, // Time in millis when the last text was sent
    val lastMessageType: MessageType = MessageType.Text,

    /** Chat can be disabled if:
     * 1) Either user deletes their account
     * 2) Either user blocks the chat { in which case, it can be unblocked in future }
     */
    var isDisabled: Boolean = false
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
            UUID.randomUUID().toString(),
            MiniUser("Andrew", "1234", null),
            MiniUser("Diana", "0000", null),
            6,
            "Wanna come over for lunch?",
            "0000",
            MessageStatus.SENT,
            System.currentTimeMillis()
        )
    }
}


