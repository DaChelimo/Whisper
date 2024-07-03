package com.da_chelimo.whisper.chats.presentation.actual_chat.screens

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.chats.domain.MessageType
import com.da_chelimo.whisper.chats.domain.toMessageType
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.AudioRecordingBar
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.ChatTopBar
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.DaySeparatorForActualChat
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.TypeMessageBar
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.messages.AudioMessage
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.messages.TextOrImageMessage
import com.da_chelimo.whisper.chats.presentation.chat_details.components.ComingSoonPopup
import com.da_chelimo.whisper.chats.repo.audio_messages.player.PlayerState
import com.da_chelimo.whisper.chats.repo.audio_messages.recorder.RecorderState
import com.da_chelimo.whisper.core.presentation.ui.ChatDetails
import com.da_chelimo.whisper.core.presentation.ui.SendImage
import com.da_chelimo.whisper.core.presentation.ui.SendImageIn
import com.da_chelimo.whisper.core.presentation.ui.ViewImage
import com.da_chelimo.whisper.core.presentation.ui.navigateSafely
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.ErrorRed
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.core.presentation.ui.theme.StatusBars
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

/**
 *
 * @param updateStatusBar - Color, Color is Bar Color and useDarkIcons
 */
@Composable
fun ActualChatScreen(
    navController: NavController,
    viewModel: ActualChatViewModel = koinViewModel(),
    chatID: String? = null,
    newContact: String? = null,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    updateStatusBar: (StatusBars) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val composeMessage by viewModel.textMessage.collectAsState()
    val otherUser by viewModel.otherUser.collectAsState()
    val otherUserLastSeen by viewModel.otherUserLastSeen.collectAsState(initial = null)
    val doesOtherUserAccountExist by viewModel.doesOtherUserAccountExist.collectAsState()

    val chat by viewModel.chat.collectAsState()

    val recorderState by viewModel.recorderState.collectAsState()
    val formattedAudioDuration by viewModel.formattedRecordingDuration.collectAsState(initial = "0:00")

    val isEditing by viewModel.isEditing.collectAsState()
    var messageIDInFocus by remember {
        mutableStateOf<String?>(null)
    }

    val audioBeingPlayed by viewModel.audioBeingPlayed.collectAsState()
    val playerState by viewModel.playerState.collectAsState()
//    val formattedPlayerTimeLeft by viewModel.formattedPlayerTimeLeft.collectAsState(null)
    val playerTimeLeftInMillis by viewModel.playerTimeLeftInMillis.collectAsState(initial = null)

    var showComingSoonPopup by remember {
        mutableStateOf(false)
    }


    val appColors = LocalAppColors.current
    val isDarkIcons = !isSystemInDarkTheme()

    // Reset the status bar color to the background color
    LaunchedEffect(key1 = Unit) {
        updateStatusBar(StatusBars(appColors.mainBackground, isDarkIcons))
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.loadChat(chatID)
        viewModel.loadOtherUser(chatID, newContact)
        viewModel.markMessagesAsOpened(chatID)
        viewModel.fetchChats(chatID)
    }


    var shouldNavigateBack by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = shouldNavigateBack) {
        if (shouldNavigateBack)
            navController.popBackStack()
    }



    BackHandler {
        viewModel.resetUnreadMessagesCountOnChatExit()
        shouldNavigateBack = true
    }


    Box {
        Column(
            Modifier
                .fillMaxSize()
                .background(LocalAppColors.current.mainBackground)
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
                    .background(LocalAppColors.current.lighterMainBackground)
                    .padding(bottom = 6.dp)
            ) {

                ChatTopBar(
                    onBackPress = {
                        viewModel.resetUnreadMessagesCountOnChatExit()
                        shouldNavigateBack = true
                    },
                    otherPersonName = otherUser?.name,
                    lastSeenOrIsOnline = otherUserLastSeen,
                    modifier = Modifier.clickable {
                        val otherUID = otherUser?.uid
                        if (viewModel.chatID != null && otherUID != null)
                            navController.navigateSafely(ChatDetails(viewModel.chatID!!, otherUID))
                    },
                    onVoiceCall = {
                        showComingSoonPopup = true
                    },
                    onVideoCall = {
                        showComingSoonPopup = true
                    }
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
                        val toggleOptionsMenuVisibility: (String?) -> Unit =
                            { messageIDInFocus = it }
                        val copyToClipboard: (String) -> Unit = {
                            clipboardManager.setText(buildAnnotatedString { append(it) })
                        }
                        val editMessage: (String) -> Unit = {
                            viewModel.launchMessageEditing(message.messageID, it)
                        }
                        val unSendMessage: (String) -> Unit = { messageID ->
                            viewModel.unsendMessage(messageID)
                            messageIDInFocus = null
                        }

                        val messageType = message.messageType.toMessageType()
                        if (messageType is MessageType.Audio) {
                            AudioMessage(
                                message = message,
                                messageIDInFocus = messageIDInFocus,
                                toggleOptionsMenuVisibility = toggleOptionsMenuVisibility,
                                unSendMessage = unSendMessage,
                                isPlaying = playerState == PlayerState.Ongoing,
                                audioUrlBeingPlayed = audioBeingPlayed,
                                timeLeftInMillis = playerTimeLeftInMillis,
                                onPlayOrPause = {
                                    viewModel.playOrPauseAudio(
                                        context = context,
                                        audioUrl = messageType.audioUrl
                                    )
                                },
                                onSeekTo = {
                                    // TODO:
                                }
                            )
                        } else if (messageType is MessageType.Text || messageType is MessageType.Image) {
                            TextOrImageMessage(
                                message = message,
                                messageIDInFocus = messageIDInFocus,
                                toggleOptionsMenuVisibility = toggleOptionsMenuVisibility,
                                copyToClipboard = copyToClipboard,
                                editMessage = editMessage,
                                unSendMessage = unSendMessage,
                                openImage = { openImage(it) }
                            )
                        }

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
                        navController.navigateSafely(
                            SendImage(
                                imageUri = imageUri.toString(),
                                sendImageIn = SendImageIn.Chat(chatID)
//                                onSendImage = { imageCaption ->
//                                    coroutineScope.launch {
//                                        viewModel.sendImage(imageUri.toString(), imageCaption)
//                                    }
//                                }
                            )
                        )
                    }
                }


            val shouldOpenMediaPicker by viewModel.openMediaPicker.collectAsState()
            LaunchedEffect(key1 = shouldOpenMediaPicker) {
                if (shouldOpenMediaPicker)
                    permissionLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

                viewModel.updateOpenMediaPicker(false)
            }


            var shouldRequestRecordingPermission by remember {
                mutableStateOf(false)
            }
            val recorderLauncher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted)
                        viewModel.startRecording(context)
                    else
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                context.getString(R.string.recording_permission_needed)
                            )
                        }

                    shouldRequestRecordingPermission = false
                }


            LaunchedEffect(key1 = shouldRequestRecordingPermission) {
                if (shouldRequestRecordingPermission)
                    recorderLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }

            // When no recording is being done or a recording has already been
            // completed and is being sent
            if (recorderState is RecorderState.None || recorderState is RecorderState.Ended) {
                TypeMessageBar(
                    value = composeMessage,
                    onValueChange = { viewModel.updateComposeMessage(it) },
                    sendMessage = {
                        viewModel.sendOrEditMessage()
                    },
                    openMediaSelector = {
                        viewModel.updateOpenMediaPicker(true)
                    },
                    startAudioRecording = {
                        shouldRequestRecordingPermission = true
                    },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .focusRequester(focusRequester)
                )
            } else {
                AudioRecordingBar(
                    cancelRecording = {
                        viewModel.cancelRecording()
                    },
                    isPaused = recorderState == RecorderState.Paused,
                    formattedAudioDuration = formattedAudioDuration,
                    onPauseOrResume = {
                        viewModel.pauseOrResumeRecording()
                    },
                    onSendAudio = {
                        viewModel.sendRecording()
                    }
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
        ActualChatScreen(
            rememberNavController(),
            snackbarHostState = snackbarHostState,
            coroutineScope = rememberCoroutineScope()
        ) { }
    }
}
