package com.da_chelimo.whisper.network_moniter

import com.da_chelimo.whisper.core.repo.user_details.UserDetailsRepo
import com.da_chelimo.whisper.core.repo.user_details.UserDetailsRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserStatusMoniter(
    private val userDetailsRepo: UserDetailsRepo = UserDetailsRepoImpl()
) {

    private var job: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun moniter() {
        job = GlobalScope.launch {
            Firebase.auth.uid?.let { userDetailsRepo.goOnline(it) }

            while (true) {
                Firebase.auth.uid?.let { uid ->
                    userDetailsRepo.updateUserLastSeen(uid, System.currentTimeMillis())
                }
                delay(5000)
            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun removeMoniter() {
        GlobalScope.launch {
            Firebase.auth.uid?.let { userDetailsRepo.goOffline(it) }
            job?.cancel()
        }
    }
}