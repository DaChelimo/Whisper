package com.da_chelimo.whisper.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.EnterNumber
import com.da_chelimo.whisper.core.presentation.ui.navigateSafely
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.core.presentation.ui.theme.StatusBars

@Composable
fun WelcomeScreen(
    navController: NavController,
    updateStatusBar: (StatusBars) -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.mainBackground)
    ) {
        LocalView.current
        val appColors = LocalAppColors.current
        val isDarkIcons = !isSystemInDarkTheme()

        // Reset the status bar color to the background color
        LaunchedEffect(key1 = Unit) {
            updateStatusBar(StatusBars(appColors.mainBackground, isDarkIcons))
        }


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.whisper_logo),
                contentDescription = null,
                modifier = Modifier.height(120.dp),
                colorFilter = ColorFilter.tint(LocalAppColors.current.appThemeTextColor)
            )

            Text(
                text = stringResource(id = R.string.welcome_message),
                modifier = Modifier.padding(top = 36.dp),
                fontFamily = QuickSand,
                fontWeight = FontWeight.SemiBold,
                color = LocalAppColors.current.plainTextColorOnMainBackground,
                fontSize = 24.sp
            )
        }

        Button(
            onClick = {
                navController.navigateSafely(EnterNumber)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LocalAppColors.current.blueCardColor,
                contentColor = Color.White
            )
        ) {
            Text(
                text = stringResource(id = R.string.register),
                fontFamily = Poppins,
                fontSize = 15.sp,
                modifier = Modifier.padding(vertical = 6.dp),
            )
        }
    }
}


@Preview
@Composable
private fun PreviewWelcomeScreen() = AppTheme {
    WelcomeScreen(rememberNavController()) {}
}