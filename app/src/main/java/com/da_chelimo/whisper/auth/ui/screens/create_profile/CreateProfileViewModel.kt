package com.da_chelimo.whisper.auth.ui.screens.create_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateProfileViewModel(
    private val userRepo: UserRepo = UserRepoImpl()
) : ViewModel() {

    private val _profilePic = MutableStateFlow<Uri?>(null)
    val profilePic: StateFlow<Uri?> = _profilePic

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _taskState = MutableStateFlow<TaskState>(TaskState.NONE)
    val taskState: StateFlow<TaskState> = _taskState

    fun createUserProfile(phoneNumber: String) {
        val profilePicLocalUri = profilePic.value

        viewModelScope.launch {
            _taskState.value = TaskState.LOADING()

            userRepo.createUser(
                name = name.value,
                phoneNumber = phoneNumber,
                profilePicLocalUri = profilePicLocalUri,
                onComplete = {
                    _taskState.value = it
                }
            )
        }
    }

    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updateProfilePic(newPic: Uri) {
        _profilePic.value = newPic
    }


    fun resetTaskState() {
        _taskState.value = TaskState.NONE
    }

}