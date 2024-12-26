package com.da_chelimo.whisper.settings.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo
import com.da_chelimo.whisper.chats.repo.chats.ChatRepoImpl
import com.da_chelimo.whisper.core.presentation.ui.theme.Theme
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.da_chelimo.whisper.settings.repo.SettingsDataStore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class SettingsViewModel(
    private val userRepo: UserRepo = UserRepoImpl(),
    private val chatRepo: ChatRepo = ChatRepoImpl(userRepo),
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val isDarkTheme = settingsDataStore.isDarkTheme.map { it == true }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _isDeletingAccount = MutableStateFlow(false)
    val isDeletingAccount: StateFlow<Boolean> = _isDeletingAccount

    fun toggleTheme(newIsDarkTheme: Boolean) = viewModelScope.launch {
        settingsDataStore.updateTheme(if (newIsDarkTheme) Theme.Dark else Theme.Light)
    }


    fun signOut() {
        Firebase.auth.signOut()
    }

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