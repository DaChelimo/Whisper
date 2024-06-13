package com.da_chelimo.whisper.chats.presentation.actual_chat.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.ChatTopBar
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.DaySeparatorForActualChat
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.MyChat
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.OtherChat
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.TypeMessageBar
import com.da_chelimo.whisper.core.presentation.ui.ChatDetails
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun ActualChatScreen(
    navController: NavController,
    viewModel: ActualChatViewModel = koinViewModel(),
    chatID: String? = null,
    newContact: String? = null
) {
    val clipboardManager = LocalClipboardManager.current
    val composeMessage by viewModel.textMessage.collectAsState()
    val otherUser by viewModel.otherUser.collectAsState()

    val isEditing by viewModel.isEditing.collectAsState()
    var messageIDInFocus by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadOtherUser(chatID, newContact)
        viewModel.fetchChats(chatID)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .imePadding()
    ) {
        val bottomRoundedShape = RoundedCornerShape(
            topStartPercent = 0,
            topEndPercent = 0,
            bottomEndPercent = 5,
            bottomStartPercent = 5
        )

        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(bottomRoundedShape)
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = 6.dp)
        ) {

            ChatTopBar(
                navController = navController,
                otherPersonName = otherUser?.name,
                modifier = Modifier.clickable {
                    val otherUID = otherUser?.uid
                    if (chatID != null && otherUID != null)
                        navController.navigate(ChatDetails(chatID, otherUID))
                },
                onVoiceCall = {},
                onVideoCall = {}
            )

            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
            )

            LazyColumn(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .clickable(null, null, onClick = { messageIDInFocus = null }),
                reverseLayout = true
            ) {
                items(viewModel.messages) { message ->
                    if (message.senderID == Firebase.auth.uid)
                        MyChat(
                            message = message,
                            messageIDInFocus = messageIDInFocus,
                            toggleOptionsMenuVisibility = { messageIDInFocus = it },
                            copyToClipboard = { messageText ->
                                clipboardManager.setText(
                                    buildAnnotatedString { append(messageText) }
                                )
                            },
                            editMessage = { oldMessage ->
                                viewModel.launchMessageEditing(message.messageID, oldMessage)
                            },
                            unSendMessage = { messageID ->
                                viewModel.unsendMessage(messageID)
                                messageIDInFocus = null
                            }
                        )
                    else
                        OtherChat(
                            message = message,
                            messageIDInFocus = messageIDInFocus,
                            toggleOptionsMenuVisibility = { messageIDInFocus = it },
                            copyToClipboard = { messageText ->
                                clipboardManager.setText(
                                    buildAnnotatedString { append(messageText) }
                                )
                                messageIDInFocus = null
                            },
                        )

                    Spacer(modifier = Modifier.height(2.dp))

                    DaySeparatorForActualChat(
                        mapOfMessageIDAndDateInString = viewModel.mapOfMessageIDAndDateInString,
                        message = message
                    )
                }
            }

        }

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        LaunchedEffect(key1 = isEditing) {
            if (isEditing != null) {
                Timber.d("isEditing is $isEditing")
                focusRequester.requestFocus()
            } else {
                focusManager.clearFocus(force = true)
                messageIDInFocus = null
            }
        }

        TypeMessageBar(
            value = composeMessage,
            onValueChange = { viewModel.updateComposeMessage(it) },
            sendMessage = {
                viewModel.sendOrEditMessage()
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .focusRequester(focusRequester)
        )
    }
}


@Preview
@Composable
private fun PreviewActualChatScreen() = AppTheme {
    ActualChatScreen(rememberNavController())
}
