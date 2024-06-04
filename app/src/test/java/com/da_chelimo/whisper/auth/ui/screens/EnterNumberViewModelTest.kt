package com.da_chelimo.whisper.auth.ui.screens

import com.da_chelimo.whisper.auth.ui.VerifyState
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.auth.ui.screens.enter_number.EnterNumberViewModel

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

        assertThat(enterNumberViewModel.verifyNumber())
            .isInstanceOf(VerifyState.Success::class.java)
    }

    @Test
    fun verifyNumber_whenNumberIsShort_returnsError() {
        val shortNumber = "79494"
        enterNumberViewModel.updateNumber(shortNumber)

        assertThat(enterNumberViewModel.verifyNumber().messageRes)
            .isEqualTo(R.string.number_too_short)
    }

    @Test
    fun verifyNumber_whenNumberIsLong_returnsError() {
        val longNumber = "7949401234"
        enterNumberViewModel.updateNumber(longNumber)

        assertThat(enterNumberViewModel.verifyNumber().messageRes)
            .isEqualTo(R.string.number_too_long)
    }

}