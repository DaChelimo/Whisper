package com.da_chelimo.whisper.notifications

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.da_chelimo.whisper.chats.repo.unread_messages.UnreadMessagesRepo
import com.da_chelimo.whisper.chats.repo.unread_messages.UnreadMessagesRepoImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import timber.log.Timber

class UnreadMessagesService(
    private val unreadMessagesRepo: UnreadMessagesRepo = UnreadMessagesRepoImpl()
): Service() {

    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onBind(intent: Intent?): IBinder? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("UnreadMessagesService.onStartCommand called")
        unreadMessagesRepo.getUnreadMessages().launchIn(coroutineScope).start()

        return START_NOT_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        coroutineScope.cancel()
    }

}