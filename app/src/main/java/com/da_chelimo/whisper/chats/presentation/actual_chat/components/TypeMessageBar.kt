package com.da_chelimo.whisper.chats.presentation.actual_chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.compose_ccp.theme.DarkBlue
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import timber.log.Timber

// TODO: Add drafts as a feature... If user leaves the chat, they can still find the draft
@Composable
fun TypeMessageBar(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    openMediaSelector: (() -> Unit)?,
    sendMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(DarkBlue),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp),
            textStyle = TextStyle.Default.copy(
                fontSize = 15.sp,
                fontFamily = QuickSand
            ),
            maxLines = 4,
            colors = TextFieldDefaults.colors(
                cursorColor = Color.White,
                selectionColors = TextSelectionColors(
                    handleColor = LocalAppColors.current.appThemeTextColor,
                    backgroundColor = LocalAppColors.current.selectionColor
                ),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,

                unfocusedContainerColor = DarkBlue,
                focusedContainerColor = DarkBlue
            ),
            keyboardActions = KeyboardActions(
                onDone = { Timber.d("OnDone called") }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences)
        )

        if (openMediaSelector != null) {
            IconButton(onClick = { openMediaSelector() }) {
                Icon(
                    painter = painterResource(id = R.drawable.attach_file),
                    contentDescription = stringResource(R.string.open_media),
                    modifier = Modifier
                        .size(28.dp)
                        .padding(2.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        IconButton(onClick = { sendMessage(value.text) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Send,
                contentDescription = stringResource(R.string.send_message),
                modifier = Modifier
                    .size(28.dp)
                    .padding(2.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }

}


@Preview
@Composable
private fun PreviewTypeMessageBar() = AppTheme {
    var typedMessage by remember { mutableStateOf(TextFieldValue("")) }

    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 10.dp)
    )
    {
        TypeMessageBar(
            value = typedMessage,
            onValueChange = { typedMessage = it },
            sendMessage = { },
            openMediaSelector = { }
        )
    }
}