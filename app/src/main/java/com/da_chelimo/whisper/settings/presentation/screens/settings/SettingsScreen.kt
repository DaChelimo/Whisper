package com.da_chelimo.whisper.settings.presentation.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.AllChats
import com.da_chelimo.whisper.core.presentation.ui.MyProfile
import com.da_chelimo.whisper.core.presentation.ui.Welcome
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.components.LoadingSpinner
import com.da_chelimo.whisper.core.presentation.ui.components.TintedAppBarIcon
import com.da_chelimo.whisper.core.presentation.ui.navigateSafely
import com.da_chelimo.whisper.core.presentation.ui.navigateSafelyAndPopTo
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.settings.presentation.components.AreYouSurePopup
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel = koinViewModel<SettingsViewModel>()
    val isDeleting by viewModel.isDeletingAccount.collectAsState()
    val isDarkMode by viewModel.isDarkTheme.collectAsState()


    var showAreYouSurePopup by remember {
        mutableStateOf(false)
    }


    var shouldNavigateBack by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = shouldNavigateBack) {
        if (shouldNavigateBack)
            navController.popBackStack()
    }



    Box(modifier = Modifier.fillMaxSize()) {
        DefaultScreen(
            backgroundColor = LocalAppColors.current.lighterMainBackground,
            appBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    TintedAppBarIcon(
                        modifier = Modifier.align(Alignment.CenterStart),
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.back_button),
                        onClick = {
                            shouldNavigateBack = true
                        }
                    )

                    Text(
                        text = stringResource(id = R.string.settings),
                        fontFamily = QuickSand,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }) {

            SettingsOption(
                name = stringResource(R.string.my_profile),
                modifier = Modifier,
                onOptionSelected = {
                    navController.navigateSafely(MyProfile)
                }
            )

            SettingsOption(
                name = stringResource(R.string.sign_out),
                modifier = Modifier,
                onOptionSelected = {
                    viewModel.signOut()
                    navController.navigateSafelyAndPopTo(
                        route = Welcome,
                        popTo = AllChats,
                        isInclusive = true
                    )
                }
            )

            SettingsOption(
                name = stringResource(R.string.delete_account),
                modifier = Modifier,
                onOptionSelected = {
                    showAreYouSurePopup = true
                }
            )

        }

        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = LocalAppColors.current.mainBackground
            ),
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 12.dp)
                .align(Alignment.BottomCenter),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.dark_mode),
                    fontFamily = QuickSand,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.weight(1f))

                Switch(
                    checked = isDarkMode,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = LocalAppColors.current.switchCheckedColor,
                        checkedTrackColor = LocalAppColors.current.switchCheckedColor.copy(alpha = 0.4f),
                        uncheckedThumbColor = LocalAppColors.current.switchUncheckedColor,
                        uncheckedTrackColor = LocalAppColors.current.switchUncheckedColor.copy(alpha = 0.4f),
                        uncheckedBorderColor = Color.White.copy(alpha = 0.2f)
                    ),
                    onCheckedChange = { turnOn ->
                        Timber.d("onCheckedChange called")
                        viewModel.toggleTheme(turnOn) })
            }
        }

        if (showAreYouSurePopup)
            AreYouSurePopup(
                dismissPopup = { showAreYouSurePopup = false },
                deleteAccount = {
                    viewModel.deleteAccount()
                    navController.navigateSafelyAndPopTo(
                        route = Welcome,
                        popTo = AllChats,
                        isInclusive = true
                    )
                }
            )


        if (isDeleting) LoadingSpinner(modifier = Modifier.align(Alignment.Center))
    }
}


@Composable
fun SettingsOption(
    name: String,
    modifier: Modifier = Modifier,
    onOptionSelected: () -> Unit
) {
    Button(
        onClick = { onOptionSelected() },
        modifier = modifier
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LocalAppColors.current.mainBackground,
            contentColor = LocalAppColors.current.appThemeTextColor
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = name,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            fontFamily = QuickSand,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@Preview
@Composable
private fun PreviewSettingsScreen() = AppTheme {
    SettingsScreen(rememberNavController())
}