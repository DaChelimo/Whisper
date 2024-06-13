package com.da_chelimo.whisper.chats.presentation.actual_chat.components.messages

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.chats.domain.Message
import com.da_chelimo.whisper.chats.presentation.utils.toDayMonthAndTime
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.ErrorRed
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand


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
