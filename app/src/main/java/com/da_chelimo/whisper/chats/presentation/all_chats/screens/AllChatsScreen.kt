package com.da_chelimo.whisper.chats.presentation.all_chats.screens

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.da_chelimo.whisper.chats.presentation.all_chats.components.ChatPreview
import com.da_chelimo.whisper.chats.presentation.view_profile_pic.ControlBlurOnScreen
import com.da_chelimo.whisper.core.presentation.ui.ActualChat
import com.da_chelimo.whisper.core.presentation.ui.SelectContact
import com.da_chelimo.whisper.core.presentation.ui.Settings
import com.da_chelimo.whisper.core.presentation.ui.components.AppBottomBar
import com.da_chelimo.whisper.core.presentation.ui.components.BottomBars
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.components.TintedAppBarIcon
import com.da_chelimo.whisper.core.presentation.ui.navigateSafely
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.core.presentation.ui.theme.StatusBars
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AllChatsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    updateStatusBar: (StatusBars) -> Unit
) {
    val viewModel = viewModel<AllChatsViewModel>()
    val context = LocalContext.current

    val chats by viewModel.chats.collectAsState(initial = null)
    var isProfilePicFullScreen by remember {
        mutableStateOf<String?>(null)
    }


    val appColors = LocalAppColors.current

    // Reset the status bar color to the background color
    LaunchedEffect(key1 = Unit) {
        updateStatusBar(StatusBars(appColors.blueCardColor, false))
    }


    DefaultScreen(
        appBar = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth()
            ) {

                TintedAppBarIcon(
                    modifier = Modifier.align(Alignment.CenterStart),
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = stringResource(R.string.open_menu),
                    onClick = {
                        navController.navigateSafely(
                            route = Settings
                        )
                    }
                )


                Text(
                    text = stringResource(id = R.string.messages),
                    fontFamily = QuickSand,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Center)
                )

//                TODO: Implement search functionality
//                TintedAppBarIcon(
//                    imageVector = Icons.Rounded.Search,
//                    contentDescription = stringResource(R.string.search),
//                    onClick = {
//
//                    }
//                )
            }
        },
        backgroundColor = LocalAppColors.current.mainBackground
    ) {

        ControlBlurOnScreen(
            isPictureOnFullScreen = isProfilePicFullScreen != null,
            profilePic = isProfilePicFullScreen,
            dismissPicture = { isProfilePicFullScreen = null }
        ) {
            Column(Modifier.fillMaxSize()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    // The user has NO CHATS
                    if (chats.isNullOrEmpty()) {
                        Column(Modifier.align(Alignment.Center)) {
                            Image(
                                painter = painterResource(id = R.drawable.start_chat),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(60.dp)
                                    .align(Alignment.CenterHorizontally),
                                colorFilter = ColorFilter.tint(LocalAppColors.current.appThemeTextColor)
                            )

                            Text(
                                text = stringResource(R.string.click_the_button_to_start_a_conversation),
                                fontFamily = QuickSand,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(0.85f),
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .padding(top = 24.dp)
                                    .align(Alignment.CenterHorizontally),
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    // The user has CHATS
                    else if (chats?.isNotEmpty() == true) {
                        LazyColumn(
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(chats!!) { chat ->
                                ChatPreview(
                                    chat = chat,
                                    modifier = Modifier,
                                    openProfilePic = { profilePic ->
                                        isProfilePicFullScreen = profilePic ?: ""
                                    },
                                    openChat = {
                                        navController.navigateSafely(
                                            ActualChat(chat.chatID, null)
                                        )
                                    }
                                )
                            }
                        }
                    }


//                    Card(
//                        modifier = Modifier
//                            .align(Alignment.BottomEnd)
//                            .padding(horizontal = 8.dp, vertical = 16.dp)
//                            .clickable {
//                                permissionRequestLauncher.launch(Manifest.permission.READ_CONTACTS)
//                            },
//                        shape = RoundedCornerShape(12.dp),
//                        colors = CardDefaults.cardColors(
//                            containerColor = LocalAppColors.current.fabContainerColor,
//                            contentColor = Color.White
//                        ),
//                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Rounded.Add,
//                            contentDescription = stringResource(R.string.start_a_chat),
//                            modifier = Modifier
//                                .size(48.dp)
//                                .padding(8.dp)
//                        )
//                    }
                }

                val permissionRequestLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted)
                        navController.navigateSafely(SelectContact)
                    else
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(context.getString(R.string.read_contacts_permission))
                        }
                }

                AppBottomBar(
                    currentBottomBar = BottomBars.AllChats,
                    navController = navController,
                    hasPrimaryAction = true,
                    onPrimaryAction = { permissionRequestLauncher.launch(Manifest.permission.READ_CONTACTS) })
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun PreviewAllChatsScreen() = AppTheme(darkTheme = true) {
    val snackbarHostState = SnackbarHostState()

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        AllChatsScreen(
            navController = rememberNavController(),
            snackbarHostState = snackbarHostState,
            coroutineScope = rememberCoroutineScope()
        ) {}
    }
}