package com.da_chelimo.whisper.chats.presentation.actual_chat.screens

import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.ChatTopBar
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.DaySeparatorForActualChat
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.TypeMessageBar
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.messages.MyChat
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.messages.OtherChat
import com.da_chelimo.whisper.core.presentation.ui.ChatDetails
import com.da_chelimo.whisper.core.presentation.ui.SendImage
import com.da_chelimo.whisper.core.presentation.ui.ViewImage
import com.da_chelimo.whisper.core.presentation.ui.navigateSafely
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.ErrorRed
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
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
    val doesOtherUserAccountExist by viewModel.doesOtherUserAccountExist.collectAsState()

    val chat by viewModel.chat.collectAsState()

    val isEditing by viewModel.isEditing.collectAsState()
    var messageIDInFocus by remember {
        mutableStateOf<String?>(null)
    }



    LaunchedEffect(key1 = Unit) {
        viewModel.loadChat(chatID)
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
                    if (viewModel.chatID != null && otherUID != null)
                        navController.navigateSafely(ChatDetails(viewModel.chatID!!, otherUID))
                },
                onVoiceCall = {},
                onVideoCall = {}
            )

            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
            )

            val openImage: (String) -> Unit = { imageUrl ->
                navController.navigateSafely(ViewImage(imageUrl))
            }

            LazyColumn(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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
                            },
                            openImage = { openImage(it) }
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
                            openImage = { openImage(it) }
                        )

                    Spacer(modifier = Modifier.height(2.dp))

                    DaySeparatorForActualChat(
                        mapOfMessageIDAndDateInString = viewModel.mapOfMessageIDAndDateInString,
                        message = message
                    )
                }
            }

            if (chat?.isDisabled == true) {
                val errorMessage =
                    if (doesOtherUserAccountExist) // The other user deleted his/her account
                        "Chat has been disabled"
                    else
                        "The other user's account no longer exists"

                Timber.d("errorMessage is $errorMessage")
                Text(
                    text = errorMessage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    textAlign = TextAlign.Center,
                    fontFamily = QuickSand,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = ErrorRed
                )
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


        val permissionLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { imageUri ->
                if (imageUri != null) {
                    navController.navigateSafely(SendImage(viewModel.chatID!!, imageUri.toString()))
                }
            }

        val shouldOpenMediaPicker by viewModel.openMediaPicker.collectAsState()
        LaunchedEffect(key1 = shouldOpenMediaPicker) {
            if (shouldOpenMediaPicker)
                permissionLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

            viewModel.updateOpenMediaPicker(false)
        }

        TypeMessageBar(
            value = composeMessage,
            onValueChange = { viewModel.updateComposeMessage(it) },
            sendMessage = {
                viewModel.sendOrEditMessage()
            },
            openMediaSelector = {
                viewModel.updateOpenMediaPicker(true)
            },
            modifier = Modifier
                .padding(vertical = 16.dp)
                .focusRequester(focusRequester)
        )
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
private fun PreviewActualChatScreen() = AppTheme {
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        ActualChatScreen(rememberNavController())
    }
}
