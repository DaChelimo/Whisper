package com.da_chelimo.whisper.notifications

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.MessagingStyle
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.ImageRequest
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.domain.MiniUser
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.main_activity.MainActivity
import com.da_chelimo.whisper.notifications.models.NotiMessage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import kotlin.math.absoluteValue

class AppNotificationManager(
    private val context: Context
) {

    /**
     * A map of the chatID to it's associated list of unread messages
     */
    private val notiMap: MutableMap<String, MutableList<NotiMessage>> = mutableMapOf()

    private val notificationManagerCompat by lazy { NotificationManagerCompat.from(context) }


    companion object {
        const val NOTIFICATION_CHAT_ID = "com.da_chelimo.whisper.NOTIFICATION_CHAT_ID"

        const val REPLY_KEY = "REPLY_MESSAGE_KEY"
        const val REPLY_CHAT_ID = "REPLY_CHAT_ID"
//        const val REPLY_MESSAGE_ID = "REPLY_MESSAGE_ID"

        const val CHAT_MESSAGES_CHANNEL_ID = "CHAT_MESSAGES_CHANNEL_ID"

        const val REPLY_INTENT_CODE = 1000
        const val OPEN_APP_INTENT_CODE = 2000
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannelCompat.Builder(
            CHAT_MESSAGES_CHANNEL_ID,
            NotificationManager.IMPORTANCE_HIGH
        )
            .setName(context.getString(R.string.chat_messages))
            .setDescription(context.getString(R.string.channel_for_direct_messages))
            .build()

        if (!doesNotificationChannelExist())
            notificationManagerCompat.createNotificationChannel(channel)
    }

    private fun doesNotificationChannelExist() =
        notificationManagerCompat.getNotificationChannel(CHAT_MESSAGES_CHANNEL_ID) != null


    /**
     * Make intent work
     */
    private fun getOpenAppPendingIntent(chatID: String): PendingIntent? {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(NOTIFICATION_CHAT_ID, chatID)
//            putExtras(Bundle().apply { putString(NOTIFICATION_CHAT_ID, chatID)} )
        }

        return PendingIntent.getActivity(
            context,
            OPEN_APP_INTENT_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getReplyPendingIntent(chatID: String): PendingIntent? {
        val replyIntent = Intent(context, ReplyNotificationReceiver::class.java).apply {
            putExtra(REPLY_CHAT_ID, chatID)
        }

        return PendingIntent.getBroadcast(
            context,
            REPLY_INTENT_CODE,
            replyIntent,
            PendingIntent.FLAG_MUTABLE
        )
    }


    private suspend fun getIconFromUrl(imageUrl: String): IconCompat? {
        val imageRequest = ImageRequest.Builder(context).data(imageUrl).target {
            IconCompat.createWithBitmap(it.toBitmap())
        }.build()
        return Coil.imageLoader(context).enqueue(imageRequest).job.await().drawable?.toBitmap()
            ?.let { IconCompat.createWithAdaptiveBitmap(it) }
    }

    private suspend fun getPersonFromUser(user: MiniUser?) = Person.Builder().apply {
        setName(user?.name)
        val icon = user?.profilePic?.let { getIconFromUrl(it) }
        setIcon(icon)
    }.build()



    @SuppressLint("MissingPermission")
    suspend fun sendNotification(
        chatID: String,
        listOfNotiMessage: List<NotiMessage>,
        currentUser: MiniUser?,
        otherUser: MiniUser?
    ) {
        createNotificationChannel()

        val remoteInput = RemoteInput.Builder(REPLY_KEY)
            .addExtras(Bundle().also { it.putCharSequence(REPLY_CHAT_ID, chatID) })
            .setLabel("Reply with ...")
            .build()

        val currentPerson = getPersonFromUser(currentUser)
        val otherPerson = getPersonFromUser(otherUser)

        Timber.d("currentPerson is $currentPerson")
        Timber.d("otherPerson is $otherPerson")

        Timber.d("listOfNotiMessage is $listOfNotiMessage")

        val replyAction = NotificationCompat.Action.Builder(
            /* icon = */ IconCompat.createWithResource(context, R.drawable.round_send),
            /* title = */ context.getString(R.string.reply),
            /* intent = */ getReplyPendingIntent(chatID)
        ).addRemoteInput(remoteInput).build()

        val notification = NotificationCompat.Builder(context, CHAT_MESSAGES_CHANNEL_ID)
            .setColor(DarkBlue.toArgb())
            .setAutoCancel(true)
            .setContentIntent(getOpenAppPendingIntent(chatID))
            .setSmallIcon(IconCompat.createWithResource(context, R.mipmap.app_icon_round))
            .setStyle(
                MessagingStyle(currentPerson).also {
                    listOfNotiMessage.forEach { notiMessage ->
                        Timber.d("notiMessage is $notiMessage")
                        it.addMessage(
                            notiMessage.messageContent,
                            notiMessage.timeSent,
                            if (notiMessage.senderUID == Firebase.auth.uid) currentPerson else otherPerson
                        )
                    }
                }
            )
            .addAction(replyAction)
            .build()

        val id = chatID.hashCode().absoluteValue

        Timber.d("chatID.hashCode() is $id")
        notificationManagerCompat.notify(id, notification)
    }



    fun clearNotifications() {
        notificationManagerCompat.cancelAll()
        notiMap.clear()
    }

}