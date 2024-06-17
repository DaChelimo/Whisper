package com.da_chelimo.whisper.main

import androidx.lifecycle.ViewModel
import com.da_chelimo.whisper.core.domain.User
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainViewModel(
    private val userRepo: UserRepo = UserRepoImpl()
) : ViewModel() {


    suspend fun fetchCurrentUser(): User? =
        Firebase.auth.uid?.let { uid ->
            userRepo.getUserFromUID(uid)
        }
}