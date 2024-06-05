package com.da_chelimo.whisper.auth.ui.screens

import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.auth.ui.screens.enter_number.EnterNumberViewModel
import com.da_chelimo.whisper.core.domain.TaskState
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class EnterNumberViewModelTest {

    private lateinit var enterNumberViewModel: EnterNumberViewModel

    @Before
    fun setUpViewModel() {
        enterNumberViewModel = EnterNumberViewModel()
    }

    @Test
    fun verifyNumber_whenNumberCorrect_returnsSuccess() {
        val correctNumber = "794940123"
        enterNumberViewModel.updateNumber(correctNumber)
        enterNumberViewModel.verifyNumber()

        assertThat(enterNumberViewModel.taskState.value)
            .isInstanceOf(TaskState.DONE.SUCCESS::class.java)
    }

    @Test
    fun verifyNumber_whenNumberIsShort_returnsError() {
        val shortNumber = "79494"
        enterNumberViewModel.updateNumber(shortNumber)
        enterNumberViewModel.verifyNumber()

        val errorMessage = (enterNumberViewModel.taskState.value as? TaskState.DONE.ERROR)?.errorMessageRes
        assertThat(errorMessage)
            .isEqualTo(R.string.number_too_short)
    }

    @Test
    fun verifyNumber_whenNumberIsLong_returnsError() {
        val longNumber = "7949401234"
        enterNumberViewModel.updateNumber(longNumber)
        enterNumberViewModel.verifyNumber()

        val errorMessage = (enterNumberViewModel.taskState.value as? TaskState.DONE.ERROR)?.errorMessageRes

        assertThat(errorMessage)
            .isEqualTo(R.string.number_too_long)
    }

}