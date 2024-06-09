package com.da_chelimo.whisper.chats.actual_chat.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.chats.actual_chat.components.ChatTopBar
import com.da_chelimo.whisper.chats.actual_chat.components.MyChat
import com.da_chelimo.whisper.chats.actual_chat.components.OtherChat
import com.da_chelimo.whisper.chats.actual_chat.components.TypeMessageBar
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActualChatScreen(
    navController: NavController,
    viewModel: ActualChatViewModel = koinViewModel(),
    chatID: String? = null,
    newContact: String? = null
) {
    val composeMessage by viewModel.textMessage.collectAsState()
    val user by viewModel.otherUser.collectAsState()

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
        ) {

            ChatTopBar(
                navController = navController,
                otherPersonName = user?.name,
                modifier = Modifier,
                onVoiceCall = {},
                onVideoCall = {}
            )


//            val messagesListState = rememberLazyListState()
            LazyColumn(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding(),
                reverseLayout = true,
//                state = messagesListState
            ) {
                items(viewModel.messages) { chat ->
                    if (chat.senderID == Firebase.auth.uid)
                        MyChat(message = chat)
                    else
                        OtherChat(message = chat)

                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
            
//            LaunchedEffect(key1 = viewModel.messages.size) {
//                val lastIndex = (viewModel.messages.size - 1).coerceAtLeast(0)
//                messagesListState.animateScrollToItem(lastIndex)
//            }
            
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
    ActualChatScreen(rememberNavController())
}
