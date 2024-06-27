package com.da_chelimo.whisper.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.da_chelimo.whisper.chats.domain.MessageType
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo
import com.da_chelimo.whisper.chats.repo.chats.ChatRepoImpl
import com.da_chelimo.whisper.chats.repo.messages.MessagesRepo
import com.da_chelimo.whisper.chats.repo.messages.MessagesRepoImpl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class ReplyNotificationReceiver(
    private val chatRepo: ChatRepo = ChatRepoImpl(),
    private val messagesRepo: MessagesRepo = MessagesRepoImpl(chatRepo)
): BroadcastReceiver() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context, intent: Intent) {
        val chatID = intent.extras?.getString(AppNotificationManager.REPLY_CHAT_ID)
        val replyMessage = intent.extras?.getCharSequence(AppNotificationManager.REPLY_KEY)?.toString()

        Timber.d("ChatID is $chatID & replyMessage is $replyMessage")

        GlobalScope.launch {
            messagesRepo.sendMessage(
                chatID = chatID ?: return@launch,
                messageType = MessageType.Text(replyMessage ?: return@launch)
            )
        }
    }

}