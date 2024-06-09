package com.da_chelimo.whisper.auth.ui.screens.create_profile

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.auth.ui.components.LoadingSpinner
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.presentation.ui.AllChats
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.components.UserIcon
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.presentation.ui.theme.SelectionBlue
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CreateProfileScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    phoneNumber: String
) {
    DefaultScreen(
        navController = navController,
        modifier = Modifier
            .padding(top = 12.dp)
            .padding(horizontal = 8.dp)
    ) {

        val viewModel = viewModel<CreateProfileViewModel>()

        val taskState by viewModel.taskState.collectAsState()
        val name by viewModel.name.collectAsState()
        val profilePic by viewModel.profilePic.collectAsState()

        var shouldOpenImagePicker by remember {
            mutableStateOf(false)
        }


        if (shouldOpenImagePicker) {
            val activityContract = ActivityResultContracts.PickVisualMedia()

            rememberLauncherForActivityResult(contract = activityContract) { imageUri ->
                imageUri?.let {
                    viewModel.updateProfilePic(newPic = it)
                }

                shouldOpenImagePicker = false
            }
        }


        Row(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = stringResource(R.string.profile_info),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Text(
                    text = stringResource(R.string.provide_name_and_profile_photo),
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 12.dp)
                )

                UserIcon(
                    profilePic = profilePic?.toString(),
                    iconSize = 140.dp,
                    modifier = Modifier.padding(top = 16.dp),
                    onClick = { shouldOpenImagePicker = true }
                )

                val indicatorColor = MaterialTheme.colorScheme.surface
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        viewModel.updateName(it)
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,

                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,

                        cursorColor = DarkBlue,
                        selectionColors = TextSelectionColors(
                            handleColor = MaterialTheme.colorScheme.surface,
                            backgroundColor = SelectionBlue
                        ),

                        unfocusedSupportingTextColor = indicatorColor,
                        focusedSupportingTextColor = indicatorColor,

                        unfocusedIndicatorColor = indicatorColor,
                        focusedIndicatorColor = indicatorColor
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        showKeyboardOnFocus = false
                    ),
                    placeholder = {
                        Text(
                            stringResource(R.string.enter_your_name),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .fillMaxWidth(),
                )


                Spacer(modifier = Modifier.weight(1f))


                LaunchedEffect(key1 = Unit) {
                    viewModel.taskState.collectLatest {
                        if (it is TaskState.DONE.SUCCESS)
                            navController.navigate(AllChats)
                        else if (it is TaskState.DONE.ERROR)
                            snackbarHostState.showSnackbar("Error occurred")
                    }
                }

                Button(
                    onClick = {
                        viewModel.createUserProfile(phoneNumber)
                        viewModel.resetTaskState()
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
                    enabled = name.isNotBlank(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = stringResource(R.string.next),
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }

            if (taskState == TaskState.LOADING)
                LoadingSpinner()
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun PreviewCreateProfileScreen() = AppTheme {
    val snackbarHostState = SnackbarHostState()

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        CreateProfileScreen(
            navController = rememberNavController(),
            snackbarHostState,
            phoneNumber = "+254794940110"
        )
    }
}