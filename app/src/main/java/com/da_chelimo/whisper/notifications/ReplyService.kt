package com.da_chelimo.whisper.notifications

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.da_chelimo.whisper.chats.repo.unread_messages.UnreadMessagesRepo
import com.da_chelimo.whisper.chats.repo.unread_messages.UnreadMessagesRepoImpl
import com.da_chelimo.whisper.chats.repo.unread_messages.models.UnreadMessages
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.da_chelimo.whisper.notifications.models.toNotiMessage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


class ReplyService(
    private val userRepo: UserRepo = UserRepoImpl(),
    private val unreadMessagesRepo: UnreadMessagesRepo = UnreadMessagesRepoImpl()
) : Service() {

    private val appNotificationManager: AppNotificationManager by lazy {
        AppNotificationManager(applicationContext)
    }

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)


    private var latestUnreadMessages = listOf<UnreadMessages>()


    override fun onBind(intent: Intent?): IBinder? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            Timber.d("onStartCommand on!!")

            // Confirm UID has an account (user finished setting up his profile)
            val isUserCompletelySignedIn = Firebase.auth.uid?.let { userRepo.getUserFromUID(it) } != null
            if (!isUserCompletelySignedIn)
                return@launch

            try {
                Timber.d("Reply service still activated")

                unreadMessagesRepo.getUnreadMessages().collectLatest { unreadMessagesList ->
                    Timber.d("unreadMessagesRepo.getUnreadMessages().collectLatest is $unreadMessagesList")

                    unreadMessagesList.forEach { unreadMessages ->
                        appNotificationManager.sendNotification(
                            chatID = unreadMessages.chatID,
                            listOfNotiMessage = unreadMessages.listOfMessages.map {
                                it.toNotiMessage(
                                    unreadMessages.chatID
                                )
                            },
                            currentUser = unreadMessages.currentMiniUser,
                            otherUser = unreadMessages.otherMiniUser
                        )
                    }
                }
            } catch (fe: FirebaseFirestoreException) {
                Timber.e(fe)
            }
        }

        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        coroutineScope.cancel()
    }
}