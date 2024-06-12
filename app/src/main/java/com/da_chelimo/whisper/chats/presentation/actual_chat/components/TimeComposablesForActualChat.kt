package com.da_chelimo.whisper.chats.presentation.actual_chat.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins

@Composable
fun DaySeparatorForActualChat(mapOfMessageIDAndDateInString: Map<String, String>, message: Message, modifier: Modifier = Modifier) {
    if (mapOfMessageIDAndDateInString.keys.contains(message.messageID)) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = mapOfMessageIDAndDateInString.getValue(message.messageID),
            fontFamily = Poppins,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}