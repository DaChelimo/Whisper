package com.da_chelimo.whisper.settings.presentation.screens.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.repo.user_details.UserDetailsRepo
import com.da_chelimo.whisper.core.repo.user_details.UserDetailsRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userDetailsRepo: UserDetailsRepo = UserDetailsRepoImpl()
): ViewModel() {

    val user = userDetailsRepo.getUserProfileFlow

    private val _taskState = MutableStateFlow<TaskState>(TaskState.NONE)
    val taskState: StateFlow<TaskState> = _taskState

    fun updateName(newName: String) = viewModelScope.launch {
        if (newName.isBlank()) return@launch

        userDetailsRepo.updateUserName(Firebase.auth.uid!!, newName)
    }

    fun updateBio(newBio: String) = viewModelScope.launch {
        if (newBio.isBlank()) return@launch

        userDetailsRepo.updateUserBio(Firebase.auth.uid!!, newBio)
    }


    /**
     * Upload the DP on the server. We use a 1.3 second delay to buy time for the upload to finish
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun updateProfilePic(localUri: Uri) = GlobalScope.launch {
        userDetailsRepo.updateUserProfilePic(Firebase.auth.uid!!, localUri).collectLatest { task ->
            _taskState.value = task
        }
        viewModelScope.launch {
            delay(1300)
            _taskState.value = TaskState.DONE.SUCCESS
        }
    }
}