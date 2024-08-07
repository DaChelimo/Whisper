@file:OptIn(ExperimentalGlideComposeApi::class)

package com.da_chelimo.whisper.chats.presentation.actual_chat.components.messages

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.domain.MessageType
import com.da_chelimo.whisper.chats.domain.toMessageType
import com.da_chelimo.whisper.chats.presentation.all_chats.components.NotSentIcon
import com.da_chelimo.whisper.chats.presentation.all_chats.components.SingleTick
import com.da_chelimo.whisper.chats.presentation.utils.toHourAndMinute
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.Cabin
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.utils.formatDurationInMillis
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.linc.audiowaveform.AudioWaveform
import com.linc.audiowaveform.infiniteLinearGradient
import com.linc.audiowaveform.model.AmplitudeType
import com.linc.audiowaveform.model.WaveformAlignment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import linc.com.amplituda.Amplituda
import timber.log.Timber


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextOrImageMessage(
    message: Message,
    messageIDInFocus: String?,
    toggleOptionsMenuVisibility: (String?) -> Unit,
    copyToClipboard: (String) -> Unit,
    openImage: (String) -> Unit,
    editMessage: (String) -> Unit,
    unSendMessage: (String) -> Unit,

    modifier: Modifier = Modifier,
    isMyChat: Boolean = message.senderID == Firebase.auth.uid, // Doing this allows Compose preview to work
) {
    val messageType = message.messageType.toMessageType()

    val content: @Composable ColumnScope.() -> Unit = {
        val imagePresent = messageType is MessageType.Image

        if (imagePresent) {
            GlideSubcomposition(
                model = (messageType as MessageType.Image).imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .aspectRatio((4 / 3).toFloat())
                    .combinedClickable(
                        onClick = { openImage(messageType.imageUrl) },
                        onLongClick = { toggleOptionsMenuVisibility(message.messageID) }
                    )
            ) {
                when (state) {
                    RequestState.Loading -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio((4 / 3).toFloat()),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                Modifier.size(36.dp),
                                color = if (isMyChat) Color.White else LocalAppColors.current.appThemeTextColor
                            )
                        }
                    }

                    RequestState.Failure -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio((4 / 3).toFloat()),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.error_occurred),
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    }

                    is RequestState.Success -> {
                        Image(
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        if (messageType.message.isNotBlank()) {
            Text(
                text = messageType.message,
                fontFamily = Cabin,
                fontSize = (14.25).sp,
                lineHeight = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = if (imagePresent) 4.dp else 12.dp, bottom = 10.dp)
                    .align(Alignment.Start)
            )
        }
    }

    MyOrOtherChat(
        isMyChat = isMyChat,
        modifier = modifier,
        message = message,
        messageIDInFocus = messageIDInFocus,
        toggleOptionsMenuVisibility = toggleOptionsMenuVisibility,
        copyToClipboard = copyToClipboard,
        editMessage = editMessage,
        unSendMessage = unSendMessage,
        content = content
    )
}


@Composable
fun AudioMessage(
    amplituda: Amplituda,
    message: Message,
    messageIDInFocus: String?,
    toggleOptionsMenuVisibility: (String?) -> Unit,
    unSendMessage: (String) -> Unit,
    audioUrlBeingPlayed: String?,
    timeLeftInMillis: Long?,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    isMyChat: Boolean = message.senderID == Firebase.auth.uid, // Doing this allows Compose preview to work

    onPlayOrPause: () -> Unit,
    onSeekTo: () -> Unit
) {
    val audioType = remember {
        message.messageType.toMessageType() as MessageType.Audio
    }
    val isThisAudioSelected = audioUrlBeingPlayed == audioType.audioUrl

    val content: @Composable ColumnScope.() -> Unit = {
        Row(
            modifier = modifier
                .padding(horizontal = 8.dp)
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val onSurfaceColor =
                if (isMyChat) Color.White else LocalAppColors.current.appThemeTextColor

            var amplitudes: List<Int>? = null
            LaunchedEffect(key1 = Unit) {
                launch(Dispatchers.IO) {
                    amplitudes = amplituda.processAudio(audioType.audioUrl).get().amplitudesAsList()
                    Timber.d("amplitudes are $amplitudes")
                }
            }

            Image(
                painter = painterResource(
                    id = if (isPlaying && isThisAudioSelected) R.drawable.pause else R.drawable.play
                ),
                contentDescription = stringResource(if (isPlaying) R.string.pause else R.string.resume),
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(26.dp)
                    .clickable { onPlayOrPause() },
                colorFilter = ColorFilter.tint(onSurfaceColor)
            )


            val animatedGradientBrush = Brush.infiniteLinearGradient(
                colors = listOf(Color(0xff22c1c3), Color(0xfffdbb2d)),
                animation = tween(durationMillis = 6000, easing = LinearEasing),
                width = 128F
            )
            // TODO: Use onSeekTo() in the slider that we'll put here
            var waveProgress by remember {
                mutableFloatStateOf(0.3f)
            }
            AudioWaveform(
                modifier = Modifier
                    .height(40.dp)
                    .padding(start = 8.dp)
                    .weight(1f),
                style = Fill,
                amplitudes = amplitudes ?: listOf(),
                waveformAlignment = WaveformAlignment.Center,
                amplitudeType = AmplitudeType.Avg,
                // Colors could be updated with Brush API
                progressBrush = animatedGradientBrush,
                waveformBrush = SolidColor(Color.LightGray),
                spikeWidth = 4.dp,
                spikePadding = 2.dp,
                progress = waveProgress,
                onProgressChange = {
                    waveProgress = it
                    // TODO: Implement onSeekTo here
                }
            )

            Text(
                text =
                if (isThisAudioSelected && timeLeftInMillis != null) timeLeftInMillis.formatDurationInMillis()
                else audioType.duration.formatDurationInMillis(),

                modifier = Modifier.padding(start = 8.dp),
                fontSize = 13.sp,
                color = onSurfaceColor
            )
        }
    }


    MyOrOtherChat(
        isMyChat = isMyChat,
        message = message,
        messageIDInFocus = messageIDInFocus,
        toggleOptionsMenuVisibility = toggleOptionsMenuVisibility,
        copyToClipboard = { },
        editMessage = { },
        unSendMessage = unSendMessage,
        modifier = modifier,
        content = content
    )
}


@Composable
fun MyOrOtherChat(
    isMyChat: Boolean,
    message: Message,
    messageIDInFocus: String?,
    toggleOptionsMenuVisibility: (String?) -> Unit,
    copyToClipboard: (String) -> Unit,
    editMessage: (String) -> Unit,
    unSendMessage: (String) -> Unit,
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    if (isMyChat) {
        MyChat(
            modifier = modifier,
            message = message,
            messageIDInFocus = messageIDInFocus,
            toggleOptionsMenuVisibility = toggleOptionsMenuVisibility,
            copyToClipboard = copyToClipboard,
            editMessage = editMessage,
            unSendMessage = unSendMessage,
            content = content
        )
    } else
        OtherChat(
            modifier = modifier,
            message = message,
            messageIDInFocus = messageIDInFocus,
            toggleOptionsMenuVisibility = toggleOptionsMenuVisibility,
            copyToClipboard = copyToClipboard,
            content = content
        )
}


@Composable
fun MyChat(
    modifier: Modifier = Modifier,
    message: Message,
    messageIDInFocus: String?,
    toggleOptionsMenuVisibility: (String?) -> Unit,
    copyToClipboard: (String) -> Unit,
    editMessage: (String) -> Unit,
    unSendMessage: (String) -> Unit,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    val cornerSize = 10.dp
    val edgeCornerSize = 2.dp

    ChatMessage(
        chatShape = RoundedCornerShape(
            topStart = cornerSize,
            topEnd = cornerSize,
            bottomEnd = edgeCornerSize,
            bottomStart = cornerSize
        ),
        chatColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        chatAlignment = Alignment.End,
        message = message,
        showOptionsMenu = messageIDInFocus == message.messageID,
        isMyChat = true,
        modifier = modifier,
        toggleOptionsMenuVisibility = toggleOptionsMenuVisibility,
        copyToClipboard = copyToClipboard,
        editMessage = editMessage,
        unSendMessage = unSendMessage,
        content = content
    )
}


@Composable
fun OtherChat(
    modifier: Modifier = Modifier,
    message: Message,
    messageIDInFocus: String?,
    toggleOptionsMenuVisibility: (String?) -> Unit,
    copyToClipboard: (String) -> Unit,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    val cornerSize = 10.dp
    val edgeCornerSize = 2.dp

    ChatMessage(
        chatShape = RoundedCornerShape(
            topStart = cornerSize,
            topEnd = cornerSize,
            bottomEnd = cornerSize,
            bottomStart = edgeCornerSize
        ),
        chatColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        chatAlignment = Alignment.Start,
        message = message,
        showOptionsMenu = messageIDInFocus == message.messageID,
        isMyChat = false,
        modifier = modifier,
        toggleOptionsMenuVisibility = toggleOptionsMenuVisibility,
        copyToClipboard = copyToClipboard,
        editMessage = null,
        unSendMessage = null,
        content = content
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatMessage(
    chatShape: Shape,
    chatColors: CardColors,
    chatAlignment: Alignment.Horizontal,
    message: Message,
    showOptionsMenu: Boolean,
    isMyChat: Boolean,
    modifier: Modifier = Modifier,
    toggleOptionsMenuVisibility: (String?) -> Unit,
    copyToClipboard: (String) -> Unit,
    editMessage: ((String) -> Unit)?,
    unSendMessage: ((String) -> Unit)?,
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Card(
        elevation = CardDefaults.cardElevation(
            if (showOptionsMenu) 4.dp else 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .combinedClickable(
                    null,
                    null,
                    onLongClick = {
                        toggleOptionsMenuVisibility(message.messageID)
                    },
                    onClick = {
                        toggleOptionsMenuVisibility(null)
                    }
                ),
            horizontalAlignment = chatAlignment
        ) {
            Card(
                shape = chatShape,
                colors = chatColors,
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.requiredWidthIn(min = 120.dp, max = 280.dp)
            ) {

                content()

            }

            Row(
                modifier = Modifier.align(chatAlignment),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (!showOptionsMenu) {
                    if (isMyChat && message.wasEdited)
                        Text(
                            text = stringResource(R.string.edited),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier.padding(horizontal = 2.dp),
                            fontFamily = Poppins
                        )

                    Text(
                        text = message.timeSent.toHourAndMinute(),
                        modifier = Modifier.padding(horizontal = if (isMyChat) 4.dp else 2.dp),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        fontFamily = Poppins
                    )

                    if (!isMyChat && message.wasEdited)
                        Text(
                            text = stringResource(R.string.edited),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                            modifier = Modifier.padding(horizontal = 2.dp),
                            fontFamily = Poppins
                        )
                }

                if (isMyChat) {
                    when (message.messageStatus) {
                        MessageStatus.OPENED -> Row {
                            RoundedSingleTick(borderColor = LocalAppColors.current.lighterMainBackground)
                            RoundedSingleTick(
                                modifier = Modifier.offset((-8).dp),
                                borderColor = LocalAppColors.current.lighterMainBackground
                            )
                        }

                        MessageStatus.RECEIVED -> RoundedSingleTick(borderColor = LocalAppColors.current.lighterMainBackground)

                        MessageStatus.SENT -> SingleTick(
                            color = LocalAppColors.current.appThemeTextColor,
                            modifier = Modifier.padding(end = 4.dp)
                        )

                        MessageStatus.NOT_SENT -> NotSentIcon()
                    }
                }
            }

            if (showOptionsMenu)
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 8.dp)
                        .clickable { toggleOptionsMenuVisibility(null) }) {
                    MessageOptions(
                        modifier = Modifier
                            .align(chatAlignment),
                        message = message,
                        copyToClipboard = copyToClipboard,
                        editMessage = {
                            if (editMessage != null) {
                                editMessage(message.messageType.toMessageType().message)
                            }
                        },
                        unSendMessage = unSendMessage
                    )
                }
        }
    }

    BackHandler(enabled = showOptionsMenu) {
        toggleOptionsMenuVisibility(null)
    }
}


@Composable
fun RoundedSingleTick(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    circleColor: Color = DarkBlue,
    tickColor: Color = Color.White,
    borderColor: Color
) {
    Image(
        imageVector = Icons.Default.Check,
        contentDescription = null,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(circleColor)
            .border(BorderStroke((1.25).dp, borderColor), CircleShape)
            .padding(2.dp)
            .padding(horizontal = (0.2).dp, vertical = 2.dp),
        colorFilter = ColorFilter.tint(tickColor),
        contentScale = ContentScale.Crop
    )
}


@Preview
@Composable
private fun PreviewChats() = AppTheme(darkTheme = true) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(LocalAppColors.current.lighterMainBackground)
            .padding(vertical = 20.dp)
    ) {
        val context = LocalContext.current
        val amplituda = remember { Amplituda(context) }

        var chatIDInFocus by remember {
            mutableStateOf<String?>(null)
        }

        TextOrImageMessage(
            isMyChat = true,
            message = Message.TEST_MY_Message, messageIDInFocus = chatIDInFocus,
            toggleOptionsMenuVisibility = { chatIDInFocus = it },
            copyToClipboard = { },
            unSendMessage = { }, openImage = {}, editMessage = {})

        Spacer(modifier = Modifier.height(4.dp))

        TextOrImageMessage(
            isMyChat = false, message = Message.TEST_MY_Message, messageIDInFocus = chatIDInFocus,
            toggleOptionsMenuVisibility = { chatIDInFocus = it }, openImage = {},
            copyToClipboard = { }, editMessage = {}, unSendMessage = {})


        val timeLeftInMillis by remember {
            mutableLongStateOf(12000L)
        }
        val audioUrlBeingPlayed by remember { mutableStateOf("abc") }
        var isPlaying by remember {
            mutableStateOf(false)
        }
        AudioMessage(
            amplituda = amplituda,
            isMyChat = true,
            message = Message.TEST_MY_Message.copy(
                messageType = MessageType.Audio(30_000, "abc").toFirebaseMap()
            ),
            messageIDInFocus = chatIDInFocus,
            toggleOptionsMenuVisibility = { chatIDInFocus = it },
            unSendMessage = {},
            isPlaying = isPlaying,
            audioUrlBeingPlayed = audioUrlBeingPlayed,
            onPlayOrPause = { isPlaying = !isPlaying },
            timeLeftInMillis = timeLeftInMillis,
            onSeekTo = {}
        )
        AudioMessage(
            amplituda = amplituda,
            isMyChat = false,
            message = Message.TEST_MY_Message.copy(
                messageType = MessageType.Audio(30_000, "").toFirebaseMap()
            ),
            messageIDInFocus = chatIDInFocus,
            toggleOptionsMenuVisibility = { chatIDInFocus = it },
            unSendMessage = {},
            isPlaying = isPlaying,
            audioUrlBeingPlayed = audioUrlBeingPlayed,
            onPlayOrPause = { isPlaying = !isPlaying },
            timeLeftInMillis = timeLeftInMillis,
            onSeekTo = {}
        )

        TextOrImageMessage(
            isMyChat = true,
            message = Message.LONG_TEST_MY_Message,
            messageIDInFocus = chatIDInFocus,
            toggleOptionsMenuVisibility = { chatIDInFocus = it },
            copyToClipboard = { },
            unSendMessage = { },
            openImage = {},
            editMessage = { })

        Spacer(modifier = Modifier.height(4.dp))

        TextOrImageMessage(
            isMyChat = false,
            message = Message.LONG_TEST_MY_Message,
            messageIDInFocus = chatIDInFocus,
            toggleOptionsMenuVisibility = { chatIDInFocus = it },
            openImage = {},
            copyToClipboard = { },
            editMessage = {},
            unSendMessage = {})

    }
}