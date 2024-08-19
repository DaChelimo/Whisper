package com.da_chelimo.whisper.auth.ui.screens.create_profile

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.canhub.cropper.CropImageContract
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.presentation.ui.AllChats
import com.da_chelimo.whisper.core.presentation.ui.Welcome
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.components.LoadingSpinner
import com.da_chelimo.whisper.core.presentation.ui.components.UserIcon
import com.da_chelimo.whisper.core.presentation.ui.navigateSafelyAndPopTo
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.core.presentation.ui.theme.SelectionBlue
import com.da_chelimo.whisper.core.utils.DefaultCropContract
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CreateProfileScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    phoneNumber: String
) {
    val viewModel = viewModel<CreateProfileViewModel>()
    val taskState by viewModel.taskState.collectAsState()

    Box(
        Modifier
            .fillMaxSize()
    ) {
        DefaultScreen(
            navController = navController,
            modifier = Modifier
                .padding(top = 4.dp)
                .padding(horizontal = 8.dp)
                .imePadding()
        ) {

            val name by viewModel.name.collectAsState()
            val bio by viewModel.bio.collectAsState()
            val profilePic by viewModel.profilePic.collectAsState()

            var galleryIsOpen by remember { mutableStateOf(false) }
            var shouldOpenGallery by remember { mutableStateOf(false) }


            val launcher =
                rememberLauncherForActivityResult(contract = CropImageContract()) { imageURI ->
                    galleryIsOpen = false

                    imageURI.uriContent?.let { uri ->
                        viewModel.updateProfilePic(uri)
                    }
                }

            LaunchedEffect(key1 = shouldOpenGallery) {
                // If we should open gallery && it was already closed, open gallery
                if (shouldOpenGallery && !galleryIsOpen) {
                    galleryIsOpen = true
                    launcher.launch(DefaultCropContract)
                }
            }


            val scrollState = rememberScrollState()


            Box(Modifier.fillMaxSize()) {
                if (taskState is TaskState.NONE) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
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
                                .padding(top = 6.dp)
                        )

                        Box(Modifier) {
                            UserIcon(
                                profilePic = profilePic?.toString(),
                                iconSize = 120.dp,
                                progressBarSize = 32.dp,
                                progressBarThickness = 3.dp,
                                modifier = Modifier.padding(top = 8.dp),
                                borderIfUsingDefaultPic = 2.dp,
                                onClick = {
                                    shouldOpenGallery = true
                                }
                            )

                            IconButton(
                                onClick = { shouldOpenGallery = true },
                                modifier = Modifier.align(Alignment.BottomEnd),
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = DarkBlue,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Add,
                                    contentDescription = stringResource(R.string.select_profile_picture_from_galley),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }

                        CreateProfileTextField(
                            modifier = Modifier.padding(top = 20.dp),
                            value = name,
                            placeHolderText = stringResource(R.string.enter_your_name),
                            onValueChange = {
                                viewModel.updateName(it)
                            }
                        )

                        CreateProfileTextField(
                            modifier = Modifier.padding(top = 12.dp),
                            value = bio,
                            placeHolderText = stringResource(id = R.string.enter_your_bio),
                            onValueChange = {
                                viewModel.updateBio(it)
                            }
                        )


                        Spacer(modifier = Modifier.weight(1f))


                        LaunchedEffect(key1 = Unit) {
                            viewModel.taskState.collectLatest {
                                if (it is TaskState.DONE.SUCCESS)
                                    navController.navigateSafelyAndPopTo(
                                        route = AllChats,
                                        popTo = Welcome,
                                        isInclusive = true
                                    )
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
                                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(
                                    0.75f
                                ),
                                disabledContainerColor = MaterialTheme.colorScheme.surface.copy(
                                    alpha = 0.75f
                                )
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
                } else
                    LoadingSpinner(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun CreateProfileTextField(
    value: String,
    placeHolderText: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    val indicatorColor = MaterialTheme.colorScheme.surface
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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
        textStyle = TextStyle(
            fontFamily = QuickSand,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        ),
        placeholder = {
            Text(
                placeHolderText,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            )
        },
        modifier = modifier
            .fillMaxWidth(),
    )
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