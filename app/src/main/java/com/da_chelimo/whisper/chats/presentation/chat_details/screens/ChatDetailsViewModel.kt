package com.da_chelimo.whisper.chats.presentation.chat_details.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.core.domain.User
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatDetailsViewModel(
    private val userRepo: UserRepo = UserRepoImpl()
): ViewModel() {

    private val _otherUser = MutableStateFlow<User?>(null)
    val otherUser: StateFlow<User?> = _otherUser


    fun loadOtherUser(userID: String) {
        viewModelScope.launch {
            _otherUser.value = userRepo.getUserFromUID(userID)
        }
    }

}