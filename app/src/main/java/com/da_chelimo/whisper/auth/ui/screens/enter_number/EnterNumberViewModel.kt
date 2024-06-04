package com.da_chelimo.whisper.auth.ui.screens.enter_number

import androidx.lifecycle.ViewModel
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.auth.ui.VerifyState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EnterNumberViewModel: ViewModel() {

    private val _number = MutableStateFlow("")
    val number: StateFlow<String> = _number

    private val _verifyState = MutableStateFlow<VerifyState?>(null)
    val verifyState: StateFlow<VerifyState?> = _verifyState

    private val _shouldNavigateToEnterCode = MutableStateFlow(false)
    val shouldNavigateToEnterCode: StateFlow<Boolean> = _shouldNavigateToEnterCode

    fun updateNumber(newNumber: String) {
        _number.value = newNumber
    }

    fun verifyNumber(): VerifyState {
        val correctDigitCount = 9
        return when {
            number.value.count() < correctDigitCount -> VerifyState.Error(R.string.number_too_short)
            number.value.count() > correctDigitCount -> VerifyState.Error(R.string.number_too_long)

            else -> VerifyState.Success()
        }
    }

    fun requestCode() {
        if (verifyState.value is VerifyState.Success)
            _shouldNavigateToEnterCode.value = true
    }

    fun resetShouldNavigate() {
        _shouldNavigateToEnterCode.value = false
    }

}