package com.da_chelimo.whisper.chats.presentation.all_chats.screens

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.da_chelimo.whisper.chats.presentation.all_chats.components.ChatPreview
import com.da_chelimo.whisper.chats.presentation.view_profile_pic.ControlBlurOnScreen
import com.da_chelimo.whisper.core.presentation.ui.ActualChat
import com.da_chelimo.whisper.core.presentation.ui.SelectContact
import com.da_chelimo.whisper.core.presentation.ui.Settings
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.components.TintedAppBarIcon
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.LightWhite
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AllChatsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    val viewModel = viewModel<AllChatsViewModel>()
    val context = LocalContext.current

    val chats by viewModel.chats.collectAsState(initial = listOf())
    var isProfilePicFullScreen by remember {
        mutableStateOf<String?>(null)
    }

    DefaultScreen(
        appBar = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TintedAppBarIcon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = stringResource(R.string.open_menu),
                    onClick = {
                        navController.navigate(Settings)
                    }
                )

                Text(
                    text = stringResource(id = R.string.messages),
                    fontFamily = QuickSand,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                TintedAppBarIcon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = stringResource(R.string.search),
                    onClick = {

                    }
                )
            }
        },
        backgroundColor = LightWhite
    ) {

        ControlBlurOnScreen(
            isPictureOnFullScreen = isProfilePicFullScreen != null,
            profilePic = isProfilePicFullScreen,
            dismissPicture = { isProfilePicFullScreen = null }
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(chats) { chat ->
                        ChatPreview(
                            chat = chat,
                            modifier = Modifier,
                            openProfilePic = { profilePic ->
                                if (profilePic != null)
                                    isProfilePicFullScreen = profilePic
                            },
                            openChat = {
                                navController.navigate(
                                    ActualChat(chat.chatID, null)
                                )
                            }
                        )
                    }
                }


                val permissionRequestLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted)
                        navController.navigate(SelectContact)
                    else
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(context.getString(R.string.read_contacts_permission))
                        }
                }

                Card(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                        .clickable {
                            permissionRequestLauncher.launch(Manifest.permission.READ_CONTACTS)
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceDim,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = stringResource(R.string.start_a_chat),
                        modifier = Modifier
                            .size(48.dp)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun PreviewAllChatsScreen() = AppTheme {
    val snackbarHostState = SnackbarHostState()

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) {
        AllChatsScreen(
            navController = rememberNavController(),
            snackbarHostState = snackbarHostState,
            coroutineScope = rememberCoroutineScope()
        )
    }
}