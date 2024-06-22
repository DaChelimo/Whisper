@file:OptIn(ExperimentalGlideComposeApi::class)

package com.da_chelimo.whisper.chats.presentation.actual_chat.components.messages

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.domain.MessageType
import com.da_chelimo.whisper.chats.domain.toMessageType
import com.da_chelimo.whisper.chats.presentation.utils.toHourAndMinute
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.Cabin
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.utils.formatDurationInMillis
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


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
            GlideImage(
                model = (messageType as MessageType.Image).imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                loading = placeholder {
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
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .aspectRatio((4 / 3).toFloat())
                    .clickable {
                        openImage(messageType.imageUrl)
                    },
                requestBuilderTransform = {
                    it.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                }
            )
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
    message: Message,
    messageIDInFocus: String?,
    toggleOptionsMenuVisibility: (String?) -> Unit,
    unSendMessage: (String) -> Unit,
    audioUrlBeingPlayed: String?,
    timeLeftInMillis: String?,
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
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val onSurfaceColor =
                if (isMyChat) Color.White else LocalAppColors.current.appThemeTextColor

            Image(
                painter = painterResource(
                    id = if (isPlaying && isThisAudioSelected) R.drawable.pause else R.drawable.play
                ),
                contentDescription = stringResource(if (isPlaying) R.string.pause else R.string.resume),
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onPlayOrPause() },
                colorFilter = ColorFilter.tint(onSurfaceColor)
            )

            // TODO: Use onSeekTo() in the slider that we'll put here
            Spacer(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f)
            )

            Text(
                text =
                if (isThisAudioSelected && timeLeftInMillis != null) timeLeftInMillis
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
                    if (message.messageStatus == MessageStatus.OPENED)
                        Row {
                            RoundedSingleTick(
                                modifier = Modifier
                            )
                            RoundedSingleTick(
                                modifier = Modifier.offset((-8).dp)
                            )
                        }
                    else
                        RoundedSingleTick()
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
    color: Color = LocalAppColors.current.appThemeTextColor//MaterialTheme.colorScheme.surface
) {
    Image(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = null,
        modifier = modifier
            .size(15.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background),
        colorFilter = ColorFilter.tint(color)
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


        var timeLeftInMillis by remember {
            mutableStateOf("00:12")
        }
        var audioUrlBeingPlayed by remember { mutableStateOf("abc") }
        var isPlaying by remember {
            mutableStateOf(false)
        }
        AudioMessage(
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