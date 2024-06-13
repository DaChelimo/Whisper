package com.da_chelimo.whisper.core.presentation.ui.theme

import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppRipple : RippleTheme {
    @Composable
    override fun defaultColor(): Color = DarkBlue

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleAlpha(0f, 0f, 0f, 0.4f)
}