package com.da_chelimo.whisper.core.presentation.ui.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

class AppColors (
    val plainTextColorOnMainBackground: Color,
    val mainBackground: Color,
    val lighterMainBackground: Color,
    val darkerMainBackground: Color,
    val fabContainerColor: Color,
    val appThemeTextColor: Color,
    val blueCardColor: Color,
    val selectionColor: Color,
)


val LocalAppColors: ProvidableCompositionLocal<AppColors> = staticCompositionLocalOf { LightAppColors }


val LightAppColors = AppColors(
    plainTextColorOnMainBackground = DarkBlack,
    mainBackground = Color.White,
    lighterMainBackground = LightWhite,
    darkerMainBackground = AlmostWhite,
    fabContainerColor = DarkerBlue,
    appThemeTextColor = DarkBlue,
    blueCardColor = DarkBlue,
    selectionColor = DarkBlue.copy(alpha = 0.4f)
)

val DarkAppColors = AppColors(
    plainTextColorOnMainBackground = Color.White,
    mainBackground = DarkBlack,
    lighterMainBackground = LightBlack,
    darkerMainBackground = SolidBlack,
    fabContainerColor = DarkerBlue,
    appThemeTextColor = Color.White,
    blueCardColor = DarkBlue,
    selectionColor = Color.White.copy(alpha = 0.3f)
)