package com.da_chelimo.whisper.auth.ui.screens.enter_code

import android.app.Activity
import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.auth.repo.AuthRepo
import com.da_chelimo.whisper.auth.repo.AuthRepoImpl
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.da_chelimo.whisper.core.utils.formatDurationInMillis
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    private val _timeLeftInMillis = MutableStateFlow(SMS_TIMEOUT)
    val timeLeftInMillis: StateFlow<Long> = _timeLeftInMillis

    val formattedTimeLeft = timeLeftInMillis.map { it.formatDurationInMillis() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    /**
     * The app moves to Chromes to verify if the user is real
     * Using isAuthenticating prevents double authentication
     */
    private var isAuthenticating = false


    companion object {
        const val CODE_LENGTH = 6

        const val SECOND_IN_MILLIS = 1000.toLong()
        const val SMS_TIMEOUT = 30 * SECOND_IN_MILLIS
    }




    fun updateCode(newCode: String) {
        _code.value = newCode
    }


    private fun startTimer() {
        // Reset timer
        _timeLeftInMillis.value = SMS_TIMEOUT

        val timer = object : CountDownTimer(SMS_TIMEOUT, SECOND_IN_MILLIS) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeftInMillis.value = millisUntilFinished
            }

            override fun onFinish() {
                isAuthenticating = false
            }
        }
        timer.start()
    }


    fun authenticateWithNumber(phoneNumber: String, activity: Activity?) {
        Timber.d("authenticateWithNumber called")
        if (!isAuthenticating) {
            startTimer()

            Timber.d("authenticateWithNumber called since isAuthenticating is $isAuthenticating")
            authRepo.authenticateWithNumber(
                phoneNumber = phoneNumber,
                activity = activity!!,
                onVerificationDone = { isSuccess ->
                    if (isSuccess)
                        _taskState.value = TaskState.DONE.SUCCESS
                }
            )
        }
    }

    suspend fun checkIfUserHasExistingAccount(): Boolean {
        val existingUser = Firebase.auth.uid?.let { userRepo.getUserFromUID(it) }
        val isExistingAccount = existingUser != null
        return isExistingAccount
    }

    fun submitCode() {
        _taskState.value = TaskState.LOADING()

        viewModelScope.launch {
            try {
                authRepo.submitSMSCode(code.value) { isSuccess ->
                    Timber.d("isSuccess in authRepo.submitSMSCode(code.value) is $isSuccess")

                    viewModelScope.launch {
                        if (isSuccess) {
                            _taskState.value = TaskState.DONE.SUCCESS
                        } else
                            _taskState.value = TaskState.DONE.ERROR(R.string.error_occurred)
                    }
                }
            } catch (e: Exception) { // Thrown when the wrong OTP is entered
                _taskState.value = TaskState.DONE.ERROR(R.string.wrong_otp_entered)
            }
        }
    }

    fun resetTaskState() {
        _taskState.value = TaskState.NONE
    }

}