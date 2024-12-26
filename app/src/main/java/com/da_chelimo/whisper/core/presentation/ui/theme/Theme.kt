package com.da_chelimo.whisper.core.presentation.ui.theme

import android.content.res.Configuration
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.da_chelimo.whisper.core.utils.getActivity
import com.da_chelimo.whisper.settings.repo.SettingsDataStore
import timber.log.Timber

private val DarkColorScheme = darkColorScheme(
    primary = DarkBlack,
    secondary = LightBlack,
    background = DarkBlack,

    surface = DarkBlue,
    surfaceDim = DarkBlue,
    tertiary = DarkBlue,

    onPrimary = Color.White,
    onSecondary = Color.White,
    onSurface = Color.White,
    onBackground = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = Color.White,
    background = Color.White,
    surfaceVariant = DarkBlue.copy(0.6f),


    surface = DarkBlue,
    surfaceDim = DarkerBlue,
    tertiary = DarkBlue,

    onPrimary = DarkBlack,
    onSecondary = DarkBlack,
    onSurface = Color.White,
    onBackground = DarkBlack
)


enum class Theme { Light, Dark }

@Composable
fun AppTheme(
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val settingsDataStore = remember { SettingsDataStore(context) }
    val userDarkTheme by settingsDataStore.isDarkTheme.collectAsState(initial = null)
    val isDarkTheme = userDarkTheme ?: isSystemInDarkTheme()

    LaunchedEffect(key1 = userDarkTheme, key2 = isDarkTheme) {
        Timber.d("isDarkTheme is $isDarkTheme")
        Timber.d("userDarkTheme is $userDarkTheme")
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        isDarkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current

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
        content = {
            CompositionLocalProvider(
                LocalRippleTheme provides AppRipple,
                LocalAppColors provides if (isDarkTheme) DarkAppColors else LightAppColors
            ) {
                content()
            }
        }
    )
}

data class StatusBars(val barColor: Color, val useDarkIcons: Boolean)

fun changeStatusBarColor(view: View, barColor: Color?, isDarkIcons: Boolean?) {
    val window = view.context.getActivity()?.window ?: return

    if (barColor != null && isDarkIcons != null) {
        window.statusBarColor = barColor.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isDarkIcons
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
annotation class LightPreviewMode

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class DarkPreviewMode

