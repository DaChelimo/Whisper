package com.da_chelimo.whisper.settings.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo
import com.da_chelimo.whisper.chats.repo.chats.ChatRepoImpl
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsViewModel(
    private val userRepo: UserRepo = UserRepoImpl(),
    private val chatRepo: ChatRepo = ChatRepoImpl(userRepo)
): ViewModel() {

    private val _isDeletingAccount = MutableStateFlow(false)
    val isDeletingAccount: StateFlow<Boolean> = _isDeletingAccount

    fun signOut() { Firebase.auth.signOut() }

    fun deleteAccount() = viewModelScope.launch {
        _isDeletingAccount.value = true

        Firebase.auth.uid?.let { uid ->
            val userDeleteSuccessful = userRepo.deleteUser(uid)
            Timber.d("userDeleteSuccessful is $userDeleteSuccessful")

            chatRepo.disableChatsForUser(uid)
            Firebase.auth.signOut()
        }

        _isDeletingAccount.value = false
    }
}