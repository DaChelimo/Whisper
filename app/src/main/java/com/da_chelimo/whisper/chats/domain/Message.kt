package com.da_chelimo.whisper.chats.domain

data class Message(
    val senderID: String,
    val messageID: String,
    var messageType: Map<String, Any>,
    var timeSent: Long,
    var messageStatus: MessageStatus,
    var wasEdited: Boolean = false
) {

    constructor() : this(
        "",
        "",
        MessageType.Text("").toFirebaseMap(),
        0,
        MessageStatus.SENT
    )


    companion object {
        val TEST_MY_Message = Message(
            senderID = "me",
            messageID = "message01",
            MessageType.Text("Hey there").toFirebaseMap(),
            timeSent = 12345678,
            messageStatus = MessageStatus.SENT
        )
        val LONG_TEST_MY_Message = Message(
            senderID = "me",
            messageID = "message01",
            MessageType.Text("Hey there. I'm Andrew. We met during the joint prefect's hike at Ngong hills.").toFirebaseMap(),
            timeSent = 12345678,
            messageStatus = MessageStatus.SENT
        )
        val TEST_OTHER_Message =
            Message(
                senderID = "you",
                messageID = "message01",
                MessageType.Text("Hey there").toFirebaseMap(),
                timeSent = 12345678,
                messageStatus = MessageStatus.SENT
            )
        val LONG_TEST_OTHER_Message = Message(
            senderID = "you",
            messageID = "message01",
            MessageType.Text("Hey there. I'm Andrew. We met during the joint prefect's hike at Ngong hills.").toFirebaseMap(),
            timeSent = 12345678,
            messageStatus = MessageStatus.SENT
        )

        val TEST_LIST_OF_CHATS = listOf(
            TEST_MY_Message, TEST_OTHER_Message,
            LONG_TEST_MY_Message, TEST_OTHER_Message, LONG_TEST_OTHER_Message,
            TEST_MY_Message
        )
    }

}

enum class MessageStatus {
    NOT_SENT,
    SENT,
    RECEIVED,
    OPENED
}