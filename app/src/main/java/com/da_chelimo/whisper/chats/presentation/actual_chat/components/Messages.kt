package com.da_chelimo.whisper.chats.presentation.actual_chat.components

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.presentation.utils.toDayMonthAndTime
import com.da_chelimo.whisper.chats.presentation.utils.toHourAndMinute
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.Cabin
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.ErrorRed
import com.da_chelimo.whisper.core.presentation.ui.theme.LightWhite
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand


@Composable
fun MyChat(
    modifier: Modifier = Modifier,
    message: Message,
    messageIDInFocus: String?,
    toggleOptionsMenuVisibility: (String?) -> Unit,
    copyToClipboard: (String) -> Unit,
    editMessage: (String) -> Unit,
    unSendMessage: (String) -> Unit
) {
    val cornerSize = 10.dp
    val edgeCornerSize = 2.dp

    ChatMessage(
        modifier = modifier,
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
        toggleOptionsMenuVisibility = toggleOptionsMenuVisibility,
        copyToClipboard = copyToClipboard,
        editMessage = editMessage,
        unSendMessage = unSendMessage
    )
}


@Composable
fun OtherChat(
    modifier: Modifier = Modifier,
    message: Message,
    messageIDInFocus: String?,
    toggleOptionsMenuVisibility: (String?) -> Unit,
    copyToClipboard: (String) -> Unit,
) {
    val cornerSize = 10.dp
    val edgeCornerSize = 2.dp

    ChatMessage(
        modifier = modifier,
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
        toggleOptionsMenuVisibility = toggleOptionsMenuVisibility,
        copyToClipboard = copyToClipboard,
        editMessage = null,
        unSendMessage = null
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
    unSendMessage: ((String) -> Unit)?
) {
    Card(
        elevation = CardDefaults.cardElevation(
            if (showOptionsMenu) 4.dp else 0.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
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
                modifier = Modifier.requiredWidthIn(min = 120.dp, max = 300.dp)
            ) {
                Text(
                    text = message.message,
                    fontFamily = Cabin,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .padding(top = 12.dp, bottom = 10.dp)
                        .align(Alignment.Start)
                )
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
                                editMessage(message.message)
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
    color: Color = MaterialTheme.colorScheme.surface
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


@Composable
fun MessageOptions(
    message: Message,
    modifier: Modifier = Modifier,
    copyToClipboard: (String) -> Unit,
    editMessage: (() -> Unit)?,
    unSendMessage: ((String) -> Unit)?
) {
    Card(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth(0.5f)
            .clickable { }
            .padding(vertical = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(Modifier.padding(start = 12.dp, end = 8.dp)) {
            Text(
                text = message.timeSent.toDayMonthAndTime(),
                fontFamily = QuickSand,
                modifier = Modifier.padding(vertical = 2.dp),
                fontSize = 13.sp
            )
            MessageOption(
                icon = R.drawable.reply,
                name = stringResource(R.string.reply),
                onOptionSelected = { })
            MessageOption(
                icon = R.drawable.forward,
                name = stringResource(R.string.forward),
                onOptionSelected = { })

            if (editMessage != null) {
                MessageOption(
                    icon = R.drawable.edit,
                    name = stringResource(R.string.edit),
                    onOptionSelected = { editMessage() })
            }

            MessageOption(
                icon = R.drawable.copy,
                name = stringResource(R.string.copy),
                onOptionSelected = { copyToClipboard(message.message) })

            if (unSendMessage != null) {
                MessageOption(
                    icon = R.drawable.unsend,
                    name = stringResource(R.string.unsend),
                    tint = ErrorRed,
                    onOptionSelected = { unSendMessage(message.messageID) })
            }
        }
    }
}

@Composable
fun MessageOption(
    @DrawableRes icon: Int,
    name: String,
    modifier: Modifier = Modifier,
    tint: Color = DarkBlue,
    onOptionSelected: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = { onOptionSelected() }),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = name, fontSize = 14.sp, color = tint, fontFamily = QuickSand)

        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            Modifier.size(20.dp),
            tint = tint
        )
    }
}


@Preview
@Composable
private fun PreviewChats() = AppTheme {
    Column(
        Modifier
            .fillMaxWidth()
            .background(LightWhite)
            .padding(vertical = 20.dp)
    ) {
        var chatIDInFocus by remember {
            mutableStateOf<String?>(null)
        }

        MyChat(
            message = Message.TEST_MY_Message, messageIDInFocus = chatIDInFocus,
            toggleOptionsMenuVisibility = { chatIDInFocus = it },
            copyToClipboard = { },
            unSendMessage = { }, editMessage = {})
        Spacer(modifier = Modifier.height(4.dp))

        OtherChat(message = Message.TEST_MY_Message, messageIDInFocus = chatIDInFocus,
            toggleOptionsMenuVisibility = { chatIDInFocus = it },
            copyToClipboard = { })

        MyChat(message = Message.LONG_TEST_MY_Message, messageIDInFocus = chatIDInFocus,
            toggleOptionsMenuVisibility = { chatIDInFocus = it },
            copyToClipboard = { },
            unSendMessage = { }, editMessage = { })

        Spacer(modifier = Modifier.height(4.dp))

        OtherChat(message = Message.LONG_TEST_MY_Message, messageIDInFocus = chatIDInFocus,
            toggleOptionsMenuVisibility = { chatIDInFocus = it },
            copyToClipboard = { })

    }
}