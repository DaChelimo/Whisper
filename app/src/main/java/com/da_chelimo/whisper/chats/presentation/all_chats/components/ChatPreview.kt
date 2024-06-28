package com.da_chelimo.whisper.chats.presentation.all_chats.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.domain.MessageType
import com.da_chelimo.whisper.chats.domain.toMessageType
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.messages.RoundedSingleTick
import com.da_chelimo.whisper.chats.presentation.utils.toChatPreviewTime
import com.da_chelimo.whisper.core.presentation.ui.components.UserIcon
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.Montserrat
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.core.presentation.ui.theme.Roboto
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

@Composable
fun ChatPreview(
    chat: Chat,
    modifier: Modifier = Modifier,
    openProfilePic: (String?) -> Unit,
    openChat: () -> Unit
) {
    Card(
        modifier
            .fillMaxWidth()
            .clickable { openChat() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(4.dp)
    ) {
        val otherUser =
            if (chat.firstMiniUser.uid == Firebase.auth.uid)
                chat.secondMiniUser
            else
                chat.firstMiniUser


        Row(
            Modifier
                .padding(vertical = 8.dp)
                .padding(horizontal = 8.dp)
        ) {
            UserIcon(
                profilePic = otherUser.profilePic,
                iconSize = 53.dp,
                progressBarSize = 20.dp,
                progressBarThickness = 2.dp,
                borderIfUsingDefaultPic = 1.dp,
                onClick = { openProfilePic(otherUser.profilePic) }
            )

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, top = 3.dp)
            ) {
                Row(Modifier.fillMaxWidth()) {
                    Text(
                        text = otherUser.name,
                        fontFamily = QuickSand,
                        fontSize = 17.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = chat.timeOfLastMessage?.toChatPreviewTime() ?: "",
                        fontFamily = Montserrat,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Light
                    )
                }


                val lastMessageIsMine =
                    chat.lastMessageSender == Firebase.auth.uid

                Row(Modifier.padding(top = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                    ChatPreviewTicks(
                        lastMessageIsMine = lastMessageIsMine,
                        lastMessageStatus = chat.lastMessageStatus
                    )


                    val messagePreview =
                        when (val lastMessageType = chat.lastMessageType.toMessageType()) {
                            is MessageType.Image -> stringResource(
                                R.string.image_preview,
                                lastMessageType.message.ifBlank { "Image" })

                            is MessageType.Audio -> stringResource(
                                R.string.audio_preview,
                                "Audio ${lastMessageType.message}"
                            )

                            else -> lastMessageType.message
                        }


                    Text(
                        text = messagePreview,
                        fontFamily = Roboto,
                        fontSize = (14.5).sp,
                        lineHeight = 17.sp,
                        fontWeight = FontWeight.Light,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 1.dp)
                    )

                    // TODO: Add double ticks {grey or blue at the end}
                    if (!lastMessageIsMine && chat.unreadMessagesCount != 0)
                        UnreadTextsCount(count = chat.unreadMessagesCount)
                }
            }
        }
    }
}

@Composable
fun ChatPreviewTicks(
    lastMessageIsMine: Boolean,
    lastMessageStatus: MessageStatus?,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        if (lastMessageIsMine) {
            LaunchedEffect(key1 = Unit) {
                Timber.d("lastMessageStatus is $lastMessageStatus")
            }

            when (lastMessageStatus) {
                MessageStatus.SENT -> SingleTick(modifier = Modifier.padding(end = 6.dp))
                MessageStatus.RECEIVED -> {
                    val tickColor =
                        LocalAppColors.current.plainTextColorOnMainBackground.copy(alpha = 0.7f)
                    Row {
                        SingleTick(color = tickColor)
                        SingleTick(modifier = Modifier.offset((-9).dp), color = tickColor)
                    }
                }

                else -> {
                    Row {
                        RoundedSingleTick(borderColor = LocalAppColors.current.mainBackground)
                        RoundedSingleTick(
                            modifier = Modifier.offset((-9).dp),
                            borderColor = LocalAppColors.current.lighterMainBackground
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun SingleTick(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
) {
    Image(
        imageVector = Icons.Rounded.Check,
        contentDescription = null,
        modifier = modifier.size(16.dp),
        contentScale = ContentScale.Crop,
        colorFilter = ColorFilter.tint(color)
    )
}


@Composable
fun UnreadTextsCount(count: Int, modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(bottom = 2.dp)
    ) {
        Text(
            text = count.toString(),
            color = Color.White,
            modifier = Modifier.align(Alignment.Center),
            fontFamily = QuickSand,
            fontSize = 10.sp
        )
    }
}

@Preview
@Composable
private fun PreviewChatPreview() = AppTheme {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        repeat(4) {
            ChatPreview(
                chat = Chat.TEST_CHAT,
                modifier = Modifier,
                openProfilePic = {},
                openChat = {}
            )
        }
    }
}