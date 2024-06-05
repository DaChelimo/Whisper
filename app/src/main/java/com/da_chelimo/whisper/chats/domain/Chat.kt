package com.da_chelimo.whisper.chats.domain

data class Chat(
    val senderID: String,
    val receiverID: String,
    var message: String,
    var timeSent: Long,
    var chatStatus: ChatStatus
) {

    constructor(): this("", "", "", 0, ChatStatus.SENT)

    companion object {
        val TEST_MY_CHAT = Chat("me", "you", "Hey there", 12345678, ChatStatus.RECEIVED)
        val LONG_TEST_MY_CHAT = Chat("me", "you", "Hey there. I'm Andrew. We met during the joint prefect's hike at Ngong hills.", 12345678, ChatStatus.RECEIVED)
        val TEST_OTHER_CHAT = Chat("you", "me", "Hey there", 12345678, ChatStatus.RECEIVED)
        val LONG_TEST_OTHER_CHAT = Chat("you", "me", "Hey there. I'm Andrew. We met during the joint prefect's hike at Ngong hills.", 12345678, ChatStatus.RECEIVED)

        val TEST_LIST_OF_CHATS = listOf(
            TEST_MY_CHAT, TEST_OTHER_CHAT,
            LONG_TEST_MY_CHAT, TEST_OTHER_CHAT, LONG_TEST_OTHER_CHAT,
            TEST_MY_CHAT
        )
    }

}

enum class ChatStatus {
    SENT,
    RECEIVED,
    OPENED
}