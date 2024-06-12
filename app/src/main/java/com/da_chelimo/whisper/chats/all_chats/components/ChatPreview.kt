package com.da_chelimo.whisper.chats.all_chats.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.domain.MessageStatus
import com.da_chelimo.whisper.chats.utils.toChatPreviewTime
import com.da_chelimo.whisper.core.presentation.ui.components.UserIcon
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.Montserrat
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.core.presentation.ui.theme.Roboto
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ChatPreview(
    chat: Chat,
    modifier: Modifier = Modifier,
    openProfilePic: () -> Unit,
    openChat: () -> Unit
) {
    Card(
        modifier
            .fillMaxWidth()
            .clickable { openChat() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(4.dp)
//        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                iconSize = 52.dp,
                onClick = { openProfilePic() }
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
                        text = chat.timeOfLastMessage.toChatPreviewTime(),
                        fontFamily = Montserrat,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Light
                    )
                }


                val lastMessageIsMine =
                    chat.lastMessageSender == Firebase.auth.uid

                Row(Modifier.padding(top = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                    if (lastMessageIsMine) {
                        if (chat.lastMessageStatus == MessageStatus.OPENED)
                            Row {
                                SingleTick(color = DarkBlue)
                                SingleTick(modifier = Modifier.offset((-9).dp), color = DarkBlue)
                            }
                        else
                            SingleTick(modifier = Modifier.padding(end = 6.dp))
                    }


                    Text(
                        text = chat.lastMessage,
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