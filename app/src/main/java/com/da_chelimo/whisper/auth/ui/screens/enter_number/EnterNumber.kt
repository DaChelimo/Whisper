package com.da_chelimo.whisper.auth.ui.screens.enter_number

import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.compose_ccp.components.CCPTextField
import com.da_chelimo.compose_ccp.model.PickerUtils
import com.da_chelimo.compose_ccp.theme.DarkBlue
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.presentation.ui.EnterCode
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.navigateSafely
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.core.presentation.ui.theme.StatusBars

@Composable
fun EnterNumberScreen(
    navController: NavController,
    updateStatusBar: (StatusBars) -> Unit
) {
    val context = LocalContext.current
    val viewModel = viewModel<EnterNumberViewModel>()

    val number by viewModel.number.collectAsState()
    val country by viewModel.country.collectAsState()
    val taskState by viewModel.taskState.collectAsState()
    val shouldNavigateToEnterCode by viewModel.shouldNavigateToEnterCode.collectAsState()

    val appColors = LocalAppColors.current

    // Reset the status bar color to the background color
    LaunchedEffect(key1 = Unit) {
        updateStatusBar(StatusBars(appColors.blueCardColor, false))
    }

    // Automatically selects the user's country
    LaunchedEffect(key1 = Unit) {
        val defaultCountry = PickerUtils.getDefaultCountry(context)
        viewModel.updateCountry(defaultCountry)
    }


    DefaultScreen(
        navController = navController,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .imePadding()
    ) {
        Text(
            text = stringResource(R.string.enter_your_phone_number),
            fontFamily = QuickSand,
            fontWeight = FontWeight.SemiBold,
            fontSize = 19.sp,
            color = LocalAppColors.current.plainTextColorOnMainBackground,
            modifier = Modifier
                .padding(top = 24.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = stringResource(R.string.verify_number_statement),
            fontFamily = Poppins,
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            color = LocalAppColors.current.plainTextColorOnMainBackground,
            lineHeight = 17.sp,
            modifier = Modifier.padding(top = 10.dp)
        )

        CCPTextField(
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            country = country,
            number = number,
            containerColor = LocalAppColors.current.mainBackground,
            textColor = LocalAppColors.current.plainTextColorOnMainBackground,
            defaultIndicatorColor = DarkBlue,
            isValid = taskState is TaskState.DONE.SUCCESS,
            errorMessage = (taskState as? TaskState.DONE.ERROR)?.errorMessageRes?.let {
                stringResource(it)
            },
            onCountryChange = { viewModel.updateCountry(it) },
            onNumberChange = { viewModel.updateNumber(it) }
        )

        Spacer(modifier = Modifier.weight(1f))


        LaunchedEffect(shouldNavigateToEnterCode) {
            if (shouldNavigateToEnterCode) {
                navController.navigateSafely(EnterCode(viewModel.numberWithCountryCode.value))
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
                containerColor = LocalAppColors.current.blueCardColor,
                contentColor = Color.White,
                disabledContentColor = Color.White.copy(0.5f),
                disabledContainerColor = LocalAppColors.current.blueCardColor.copy(0.5f)
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
private fun PreviewEnterNumberScreen() = AppTheme(darkTheme = true) {
    EnterNumberScreen(rememberNavController()) {}
}