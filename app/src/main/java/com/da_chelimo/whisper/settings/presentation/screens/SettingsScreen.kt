package com.da_chelimo.whisper.settings.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.AllChats
import com.da_chelimo.whisper.core.presentation.ui.Welcome
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.components.TintedAppBarIcon
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.LightWhite
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand

@Composable
fun SettingsScreen(navController: NavController) {

    val viewModel = viewModel<SettingsViewModel>()

    DefaultScreen(
        backgroundColor = LightWhite,
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
                        navController.popBackStack()
                    }
                )

                Text(
                    text = stringResource(id = R.string.settings),
                    fontFamily = QuickSand,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }) {

        Button(
            onClick = {
                viewModel.signOut()
                navController.navigate(Welcome) {
                    popUpTo(AllChats) {
                        inclusive = true
                    }
                }
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.surface
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Text(
                text = stringResource(R.string.sign_out),
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
}


@Preview
@Composable
private fun PreviewSettingsScreen() = AppTheme {
    SettingsScreen(rememberNavController())
}