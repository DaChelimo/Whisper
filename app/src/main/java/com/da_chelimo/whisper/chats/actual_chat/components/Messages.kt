package com.da_chelimo.whisper.chats.actual_chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.utils.toHourAndMinute
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.Cabin
import com.da_chelimo.whisper.core.presentation.ui.theme.LightWhite
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins


@Composable
fun MyChat(modifier: Modifier = Modifier, chat: Chat) {
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
        chat = chat
    )
}


@Composable
fun OtherChat(modifier: Modifier = Modifier, chat: Chat) {
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
        chat = chat
    )
}


@Composable
fun ChatMessage(chatShape: Shape, chatColors: CardColors, chatAlignment: Alignment.Horizontal, modifier: Modifier = Modifier, chat: Chat) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = chatAlignment
    ) {
        Card(
            shape = chatShape,
            colors = chatColors,
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.requiredWidthIn(min = 120.dp, max = 300.dp)
        ) {
            Text(
                text = chat.message,
                fontFamily = Cabin,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 12.dp, bottom = 10.dp)
                    .align(Alignment.Start)
            )
        }

        Text(
            text = chat.timeSent.toHourAndMinute(),
            modifier = Modifier.align(chatAlignment).padding(horizontal = 2.dp),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            fontFamily = Poppins
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


        MyChat(chat = Chat.TEST_MY_CHAT)
        Spacer(modifier = Modifier.height(4.dp))
        OtherChat(chat = Chat.TEST_MY_CHAT)

        MyChat(chat = Chat.LONG_TEST_MY_CHAT)
        Spacer(modifier = Modifier.height(4.dp))
        OtherChat(chat = Chat.LONG_TEST_MY_CHAT)

    }
}

@Preview
@Composable
private fun PreviewMyChat() = AppTheme {
    Column(
        Modifier
            .fillMaxWidth()
            .background(LightWhite)
            .padding(vertical = 20.dp)
    ) {
        MyChat(chat = Chat.TEST_MY_CHAT)
        Spacer(modifier = Modifier.height(4.dp))
        MyChat(chat = Chat.LONG_TEST_MY_CHAT)
    }
}