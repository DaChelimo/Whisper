package com.da_chelimo.whisper.auth.ui.screens.enter_number

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.presentation.ui.EnterCode
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.CorrectGreen
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.ErrorRed
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.core.presentation.ui.theme.SelectionBlue

@Composable
fun EnterNumberScreen(navController: NavController) {
    val viewModel = viewModel<EnterNumberViewModel>()

    val number by viewModel.number.collectAsState()
    val taskState by viewModel.taskState.collectAsState()
    val shouldNavigateToEnterCode by viewModel.shouldNavigateToEnterCode.collectAsState()

    DefaultScreen(navController = navController, modifier = Modifier.padding(horizontal = 12.dp).imePadding()) {
        Text(
            text = "Enter your phone number",
            fontFamily = QuickSand,
            fontWeight = FontWeight.SemiBold,
            fontSize = 19.sp,
            modifier = Modifier
                .padding(top = 24.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Whisper will need to verify your phone number. Carrier charges may apply",
            fontFamily = Poppins,
            fontSize = 15.sp,
            fontWeight = FontWeight.Light,
            lineHeight = 17.sp,
            modifier = Modifier
                .padding(top = 12.dp)
        )


        val indicatorColor =
            if (taskState is TaskState.DONE.SUCCESS) CorrectGreen else MaterialTheme.colorScheme.surface

        OutlinedTextField(
            value = number,
            onValueChange = {
                viewModel.updateNumber(it)
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                errorContainerColor = MaterialTheme.colorScheme.background,

                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                errorTextColor = MaterialTheme.colorScheme.onBackground,

                cursorColor = DarkBlue,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.surface,
                    backgroundColor = SelectionBlue
                ),

                unfocusedSupportingTextColor = indicatorColor,
                focusedSupportingTextColor = indicatorColor,

                unfocusedIndicatorColor = indicatorColor,
                focusedIndicatorColor = indicatorColor,

                errorIndicatorColor = ErrorRed
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                showKeyboardOnFocus = false
            ),
//            leadingIcon = {
//                // TODO: Add this
//            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            isError = taskState is TaskState.DONE.ERROR,
            supportingText = {
                val state = taskState as? TaskState.DONE.ERROR

                state?.let {
                    Text(stringResource(id = state.errorMessageRes))
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))


        LaunchedEffect(shouldNavigateToEnterCode) {
            if (shouldNavigateToEnterCode) {
                navController.navigate(EnterCode(viewModel.numberWithCountryCode.value))
                viewModel.resetShouldNavigate()
            }
        }

        Button(
            onClick = {
                viewModel.navigateToEnterCode()
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
            enabled = taskState is TaskState.DONE.SUCCESS,
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


@Preview
@Composable
private fun PreviewEnterNumberScreen() {
    AppTheme {
        EnterNumberScreen(rememberNavController())
    }
}