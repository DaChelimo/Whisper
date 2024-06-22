package com.da_chelimo.whisper.chats.domain

import com.da_chelimo.whisper.core.domain.MiniUser
import com.da_chelimo.whisper.core.domain.User
import java.util.UUID

data class Chat(
    val chatID: String,

    val firstMiniUser: MiniUser,
    val secondMiniUser: MiniUser,

    var unreadMessagesCount: Int,

    var lastMessageSender: String?,
    val lastMessageStatus: MessageStatus?,
    var timeOfLastMessage: Long?, // Time in millis when the last text was sent
    val lastMessageType: Map<String, Any>,

    /** Chat can be disabled if:
     * 1) Either user deletes their account
     * 2) Either user blocks the chat { in which case, it can be unblocked in future }
     */
    var isDisabled: Boolean = false
) {

    constructor() : this(
        chatID = "",
        firstMiniUser = MiniUser("", "", null),
        secondMiniUser = MiniUser("", "", null),
        unreadMessagesCount = 0,
        lastMessageSender = "",
        lastMessageStatus = MessageStatus.SENT,
        timeOfLastMessage = 0,
        lastMessageType = MessageType.Text("").toFirebaseMap(),
        isDisabled = false
    )

    companion object {
        fun generateNewChatUsingCurrentUser(chatID: String, currentUser: User?, otherContact: MiniUser) =
            Chat(
                chatID = chatID,

                firstMiniUser = MiniUser(currentUser!!.name, currentUser.uid, currentUser.profilePic),
                secondMiniUser = otherContact,

                unreadMessagesCount = 0,

                lastMessageSender = currentUser.uid,
                lastMessageStatus = MessageStatus.SENT,
                timeOfLastMessage = System.currentTimeMillis(),
                lastMessageType = MessageType.Text("").toFirebaseMap()
            )


        val TEST_CHAT = Chat(
            chatID = UUID.randomUUID().toString(),
            firstMiniUser = MiniUser("Andrew", "1234", null),
            secondMiniUser = MiniUser("Diana", "0000", null),
            unreadMessagesCount = 6,
            lastMessageSender = "Wanna come over for lunch?",
            lastMessageStatus = MessageStatus.SENT,
            timeOfLastMessage = System.currentTimeMillis(),
            lastMessageType = MessageType.Text("").toFirebaseMap()
        )
    }
}


