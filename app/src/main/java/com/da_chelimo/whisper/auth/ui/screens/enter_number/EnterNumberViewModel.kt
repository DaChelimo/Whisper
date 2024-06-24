package com.da_chelimo.whisper.auth.ui.screens.enter_number

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.compose_ccp.model.PickerUtils
import com.da_chelimo.compose_countrycodepicker.libs.Country
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.domain.TaskState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber

class EnterNumberViewModel: ViewModel() {

    private val _number = MutableStateFlow("")
    val number: StateFlow<String> = _number

    private val _country = MutableStateFlow(PickerUtils.defaultCountry)
    val country: StateFlow<Country> = _country

    val numberWithCountryCode = number.map { "${country.value.phoneNoCode}$it".trim() }.stateIn(viewModelScope, SharingStarted.Eagerly, "")

    private val _taskState = MutableStateFlow<TaskState?>(null)
    val taskState: StateFlow<TaskState?> = _taskState

    private val _shouldNavigateToEnterCode = MutableStateFlow(false)
    val shouldNavigateToEnterCode: StateFlow<Boolean> = _shouldNavigateToEnterCode

    fun updateNumber(newNumber: String) {
        _number.value = newNumber
        verifyNumber()
    }

    fun updateCountry(newCountry: Country) {
        _country.value = newCountry
    }

    fun verifyNumber() {
        val correctDigitCount = 9
        _taskState.value = when {
            number.value.count() < correctDigitCount -> TaskState.DONE.ERROR(R.string.number_too_short)
            number.value.count() > correctDigitCount -> TaskState.DONE.ERROR(R.string.number_too_long)

            else -> TaskState.DONE.SUCCESS
        }
    }

    fun navigateToEnterCode() {
        if (taskState.value is TaskState.DONE.SUCCESS)
            _shouldNavigateToEnterCode.value = true
        Timber.d("shouldNavigateToEnterCode.value is ${shouldNavigateToEnterCode.value}")
    }

    fun resetShouldNavigate() {
        _shouldNavigateToEnterCode.value = false
    }

}