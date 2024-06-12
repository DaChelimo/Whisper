package com.da_chelimo.whisper.chats.presentation.chat_details.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import com.da_chelimo.whisper.chats.presentation.chat_details.components.ChatDetailActions
import com.da_chelimo.whisper.chats.presentation.chat_details.components.ComingSoonPopup
import com.da_chelimo.whisper.chats.presentation.chat_details.components.RedBorderButton
import com.da_chelimo.whisper.chats.presentation.view_profile_pic.ControlBlurOnScreen
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.components.UserIcon
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand

@Composable
fun ChatDetailsScreen(chatID: String, otherUserID: String, navController: NavController) {
    val viewModel = viewModel<ChatDetailsViewModel>()

    val otherUser by viewModel.otherUser.collectAsState(initial = null)
    var showComingSoonPopup by remember {
        mutableStateOf(false)
    }
    var isProfilePicFullScreen by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadOtherUser(otherUserID)
    }


    ControlBlurOnScreen(
        isPictureOnFullScreen = isProfilePicFullScreen,
        profilePic = otherUser?.profilePic,
        dismissPicture = { isProfilePicFullScreen = false }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            DefaultScreen(navController = navController) {
                otherUser?.let { user ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        UserIcon(
                            modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                            profilePic = otherUser?.profilePic,
                            iconSize = 120.dp,
                            onClick = {
                            isProfilePicFullScreen = true
                            }
                        )

                        Text(
                            text = user.name,
                            fontFamily = QuickSand,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Text(
                            text = user.number,
                            fontFamily = QuickSand,
                            fontSize = 16.sp,
                            lineHeight = 18.sp,
                            letterSpacing = (1.2).sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
                        )

                        Text(
                            text = buildString {
                                append(" ~ ")
                                append(user.bio)
                                append(" ~ ")
                            },
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .padding(horizontal = 12.dp)
                                .fillMaxWidth(0.7f),
                            textAlign = TextAlign.Center,
                            lineHeight = 15.sp,
                            fontFamily = QuickSand,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                            fontSize = 16.sp
                        )
                    }

                    ChatDetailActions(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        onMessage = {
                            navController.popBackStack()
                        },
                        onAudioCall = { showComingSoonPopup = true },
                        onVideoCall = { showComingSoonPopup = true }
                    )


                    Spacer(modifier = Modifier.weight(1f))

                    RedBorderButton(
                        name = stringResource(R.string.block_user),
                        modifier = Modifier.padding(bottom = 4.dp),
                        onOptionSelected = {}
                    )
                    RedBorderButton(
                        name = stringResource(R.string.report_user),
                        modifier = Modifier.padding(bottom = 8.dp),
                        onOptionSelected = {}
                    )
                }
            }

            if (showComingSoonPopup)
                ComingSoonPopup(
                    hidePopup = {
                        showComingSoonPopup = false
                    }
                )
        }
    }
}


@Preview
@Composable
private fun PreviewChatDetailsScreen() = AppTheme {
    ChatDetailsScreen("", "lCfSU1UMWOh2LFsDA1bpEE8nGSx2", navController = rememberNavController())
}