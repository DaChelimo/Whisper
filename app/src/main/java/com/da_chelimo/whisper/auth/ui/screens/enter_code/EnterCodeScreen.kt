package com.da_chelimo.whisper.auth.ui.screens.enter_code

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.auth.ui.components.OTPTextField
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.presentation.ui.AllChats
import com.da_chelimo.whisper.core.presentation.ui.CreateProfile
import com.da_chelimo.whisper.core.presentation.ui.Welcome
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.components.LoadingSpinner
import com.da_chelimo.whisper.core.presentation.ui.navigateSafely
import com.da_chelimo.whisper.core.presentation.ui.navigateSafelyAndPopTo
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.core.utils.getActivity

@Composable
fun EnterCodeScreen(
    navController: NavController,
    phoneNumberWithCountryCode: String,
    snackbarHostState: SnackbarHostState
) {

    val viewModel = viewModel<EnterCodeViewModel>()
    val code by viewModel.code.collectAsState()
    val taskState by viewModel.taskState.collectAsState()

    val timeLeftInMillis by viewModel.timeLeftInMillis.collectAsState()
    val formattedTimeLeft by viewModel.formattedTimeLeft.collectAsState()

    val activity = LocalContext.current.getActivity()


    LaunchedEffect(key1 = taskState) {
        when (taskState) {
            is TaskState.NONE -> viewModel.authenticateWithNumber(
                phoneNumberWithCountryCode,
                activity
            )

            is TaskState.DONE.SUCCESS -> {
                val isExistingAccount = viewModel.checkIfUserHasExistingAccount()

                if (isExistingAccount)
                    navController.navigateSafelyAndPopTo(AllChats, Welcome, true)
                else
                    navController
                        .navigateSafely(CreateProfile(phoneNumberWithCountryCode))


                viewModel.resetTaskState()
            }

            is TaskState.DONE.ERROR -> {
                snackbarHostState.showSnackbar("Error occurred")
                viewModel.resetTaskState()
            }

            else -> {}
        }
    }

    DefaultScreen(
        navController = navController,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .imePadding()
    ) {
        if (taskState is TaskState.NONE) {
            Text(
                text = stringResource(R.string.enter_code),
                modifier = Modifier
                    .padding(top = 18.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleLarge,
                color = LocalAppColors.current.plainTextColorOnMainBackground
            )


            OTPTextField(
                value = code,
                length = EnterCodeViewModel.CODE_LENGTH,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                spacedBy = 10.dp
            ) { newCode ->
                viewModel.updateCode(newCode)
            }

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.resend_code_in, formattedTimeLeft), fontSize = 14.sp)

                Button(
                    onClick = {
                        viewModel.authenticateWithNumber(phoneNumberWithCountryCode, activity)
                    },
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = LocalAppColors.current.appThemeTextColor,
                        disabledContentColor = LocalAppColors.current.appThemeTextColor.copy(alpha = 0.65f)
                    ),
                    enabled = timeLeftInMillis < EnterCodeViewModel.SECOND_IN_MILLIS
                ) {
                    Text(
                        stringResource(R.string.resend_code),
                        fontFamily = QuickSand,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.submitCode()
                },
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(0.75f),
                    disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f)
                ),
                enabled = code.length == EnterCodeViewModel.CODE_LENGTH,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(R.string.submit_code),
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
        else if (taskState is TaskState.LOADING || taskState is TaskState.DONE.SUCCESS) {
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadingSpinner()
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun PreviewEnterCodeScreen() = AppTheme {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        EnterCodeScreen(
            navController = rememberNavController(),
            phoneNumberWithCountryCode = "",
            snackbarHostState = snackbarHostState
        )
    }
}