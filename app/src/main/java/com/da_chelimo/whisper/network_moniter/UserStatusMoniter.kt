package com.da_chelimo.whisper.network_moniter

import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.da_chelimo.whisper.core.repo.user_details.UserDetailsRepo
import com.da_chelimo.whisper.core.repo.user_details.UserDetailsRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class UserStatusMoniter(
    private val userRepo: UserRepo = UserRepoImpl(),
    private val userDetailsRepo: UserDetailsRepo = UserDetailsRepoImpl()
) {

    private var job: Job? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun moniter() {
        job = GlobalScope.launch {
            try {
                // Confirm UID has an account (user finished setting up his profile)
                val isUserCompletelySignedIn = Firebase.auth.uid?.let { userRepo.getUserFromUID(it) } != null
                if (!isUserCompletelySignedIn)
                    return@launch

                Timber.d("About to go online")
                Firebase.auth.uid?.let { userDetailsRepo.goOnline(it) }

                while (true) {
                    Firebase.auth.uid?.let { uid ->
                        userDetailsRepo.updateUserLastSeen(uid, System.currentTimeMillis())
                    }
                    delay(5000)
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    fun removeMoniter() {
        try {
            GlobalScope.launch {
                // Confirm UID has an account (user finished setting up his profile)
                val isUserCompletelySignedIn = Firebase.auth.uid?.let { userRepo.getUserFromUID(it) } != null
                if (!isUserCompletelySignedIn)
                    return@launch

                Firebase.auth.uid?.let { userDetailsRepo.goOffline(it) }
            }
            job?.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}