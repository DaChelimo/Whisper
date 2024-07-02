package com.da_chelimo.whisper.notifications

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.da_chelimo.whisper.chats.repo.unread_messages.UnreadMessagesRepo
import com.da_chelimo.whisper.chats.repo.unread_messages.UnreadMessagesRepoImpl
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

class UnreadMessagesService(
    private val userRepo: UserRepo = UserRepoImpl(),
    private val unreadMessagesRepo: UnreadMessagesRepo = UnreadMessagesRepoImpl()
) : Service() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onBind(intent: Intent?): IBinder? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("UnreadMessagesService.onStartCommand called")

        coroutineScope.launch {
            // Confirm UID has an account (user finished setting up his profile)
            val isUserCompletelySignedIn =
                Firebase.auth.uid?.let { userRepo.getUserFromUID(it) } != null
            if (!isUserCompletelySignedIn)
                return@launch

            try {
                unreadMessagesRepo.getUnreadMessages()
            } catch (fe: FirebaseFirestoreException) {
                Timber.e(fe)
            }
        }

        return START_NOT_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        coroutineScope.cancel()
    }

}