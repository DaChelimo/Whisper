package com.da_chelimo.whisper.auth.ui.screens.enter_code

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.auth.repo.AuthRepo
import com.da_chelimo.whisper.auth.repo.AuthRepoImpl
import com.da_chelimo.whisper.core.domain.TaskState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EnterCodeViewModel(
    private val authRepo: AuthRepo = AuthRepoImpl()
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

    fun submitCode() {
        viewModelScope.launch {
            _taskState.value = TaskState.LOADING

            authRepo.submitSMSCode(code.value) { isSuccess ->
                if (isSuccess)
                    _taskState.value = TaskState.DONE.SUCCESS
                else
                    _taskState.value = TaskState.DONE.ERROR(R.string.error_occurred)
            }
        }
    }


    fun resetTaskState() {
        _taskState.value = TaskState.NONE
    }
}