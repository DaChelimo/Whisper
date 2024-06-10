package com.da_chelimo.whisper.core.presentation.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.da_chelimo.whisper.core.utils.getActivity

private val DarkColorScheme = darkColorScheme(
    primary = DarkBlack,
    secondary = LightBlack,
    surface = LightBlack,
    background = DarkBlack,

    onPrimary = Color.White,
    onSecondary = Color.White,
    onSurface = Color.White,
    onBackground = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = Color.White,
    tertiary = Color.White,
    background = Color.White,
    surface = DarkBlue,
    surfaceDim = DarkerBlue,
    surfaceVariant = DarkBlue.copy(0.6f),

    onPrimary = DarkBlack,
    onSecondary = DarkBlack,
    onSurface = Color.White,
    onBackground = DarkBlack
)

@Composable
fun AppTheme(
    darkTheme: Boolean = false, // TODO : Modify for dark mode too : isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // true
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
//    if (!view.isInEditMode)
    SideEffect {
        val window = (view.context.getActivity() ?: return@SideEffect).window
        window.statusBarColor = colorScheme.surface.toArgb()
        window.navigationBarColor = colorScheme.background.toArgb()

        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
