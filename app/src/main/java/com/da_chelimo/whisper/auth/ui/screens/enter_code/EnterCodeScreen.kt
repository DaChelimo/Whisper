package com.da_chelimo.whisper.auth.ui.screens.enter_code

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.auth.ui.components.LoadingSpinner
import com.da_chelimo.whisper.auth.ui.components.OTPTextField
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.presentation.ui.CreateProfile
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.utils.getActivity

@Composable
fun EnterCodeScreen(navController: NavController, phoneNumberWithCountryCode: String) {

    val viewModel = viewModel<EnterCodeViewModel>()
    val code by viewModel.code.collectAsState()
    val taskState by viewModel.taskState.collectAsState()

    val activity = LocalContext.current.getActivity()


    LaunchedEffect(key1 = taskState) {
        when (taskState) {
            is TaskState.NONE -> viewModel.authenticateWithNumber(
                phoneNumberWithCountryCode,
                activity
            )

            is TaskState.DONE.SUCCESS -> {
                navController
                    .navigate(CreateProfile(phoneNumberWithCountryCode))
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

        if (taskState is TaskState.LOADING) {

            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadingSpinner()
            }

        } else {
            Text(
                text = stringResource(R.string.enter_code),
                modifier = Modifier
                    .padding(top = 18.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleLarge
            )


            OTPTextField(
                value = code,
                length = EnterCodeViewModel.CODE_LENGTH,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                spacedBy = 10.dp
            ) { newCode ->
                viewModel.updateCode(newCode)
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
                    text = stringResource(R.string.request_code),
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}


@Preview
@Composable
private fun PreviewEnterCodeScreen() = AppTheme {
    EnterCodeScreen(navController = rememberNavController(), "")
}