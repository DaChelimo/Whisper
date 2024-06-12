package com.da_chelimo.whisper.auth.ui.screens.enter_code

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.auth.repo.AuthRepo
import com.da_chelimo.whisper.auth.repo.AuthRepoImpl
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class EnterCodeViewModel(
    private val authRepo: AuthRepo = AuthRepoImpl(),
    private val userRepo: UserRepo = UserRepoImpl()
) : ViewModel() {

    private val _code = MutableStateFlow("")
    val code: StateFlow<String> = _code

    private val _taskState = MutableStateFlow<TaskState>(TaskState.NONE)
    val taskState: StateFlow<TaskState> = _taskState

    companion object {
        const val CODE_LENGTH = 6
    }

    fun updateCode(newCode: String) {
        _code.value = newCode
    }


    fun authenticateWithNumber(phoneNumber: String, activity: Activity?) {
        authRepo.authenticateWithNumber(
            phoneNumber = phoneNumber,
            activity = activity!!,
            onVerificationDone = { isSuccess ->
                if (isSuccess)
                    _taskState.value = TaskState.DONE.SUCCESS
            }
        )
    }

    suspend fun checkIfUserHasExistingAccount(): Boolean {
        val existingUser = Firebase.auth.uid?.let { userRepo.getUserFromUID(it) }
        val isExistingAccount = existingUser != null
        return isExistingAccount
    }

    fun submitCode() {
        viewModelScope.launch {
            _taskState.value = TaskState.LOADING()

            authRepo.submitSMSCode(code.value) { isSuccess ->
                Timber.d("isSuccess in authRepo.submitSMSCode(code.value) is $isSuccess")

                viewModelScope.launch {
                    if (isSuccess) {
                        _taskState.value = TaskState.DONE.SUCCESS
                    }
                    else
                        _taskState.value = TaskState.DONE.ERROR(R.string.error_occurred)
                }
            }
        }
    }


    fun resetTaskState() {
        _taskState.value = TaskState.NONE
    }
}