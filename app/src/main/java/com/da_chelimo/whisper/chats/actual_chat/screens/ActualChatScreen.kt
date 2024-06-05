package com.da_chelimo.whisper.chats.actual_chat.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.chats.actual_chat.components.ChatTopBar
import com.da_chelimo.whisper.chats.actual_chat.components.MyChat
import com.da_chelimo.whisper.chats.actual_chat.components.OtherChat
import com.da_chelimo.whisper.chats.actual_chat.components.TypeMessageBar
import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme

@Composable
fun ActualChatScreen(navController: NavController, chats: List<Chat>) {

    val viewModel = viewModel<ActualChatViewModel>()
    val composeMessage by viewModel.composeMessage.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
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
        ) {

            ChatTopBar(
                navController = navController,
                modifier = Modifier,
                onVoiceCall = {},
                onVideoCall = {}
            )


            LazyColumn(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxSize()) {
                items(chats) { chat ->
                    if (chat.senderID == "me")
                        MyChat(chat = chat)
                    else
                        OtherChat(chat = chat)

                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }


        TypeMessageBar(
            value = composeMessage,
            onValueChange = { viewModel.updateComposeMessage(it) },
            sendMessage = { viewModel.sendMessage() },
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}


@Preview
@Composable
private fun PreviewActualChatScreen() = AppTheme {
    ActualChatScreen(rememberNavController(), Chat.TEST_LIST_OF_CHATS)
}
