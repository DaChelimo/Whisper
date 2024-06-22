package com.da_chelimo.whisper.chats.presentation.actual_chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.compose_ccp.theme.DarkBlue
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.ErrorRed
import com.da_chelimo.whisper.core.presentation.ui.theme.LightPreviewMode
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.TypingBarBlack


@Composable
fun AudioRecordingBar(
    modifier: Modifier = Modifier,
    cancelRecording: () -> Unit,
    isPaused: Boolean,
    formattedAudioDuration: String,
    onPauseOrResume: () -> Unit,
    onSendAudio: () -> Unit
) {
    val containerColor = if (isSystemInDarkTheme()) TypingBarBlack else DarkBlue
    val roundedShape = RoundedCornerShape(12.dp, 12.dp, 0.dp, 0.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(roundedShape)
            .background(containerColor)
            .padding(horizontal = 8.dp)
            .padding(vertical = 8.dp),
    ) {
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = formattedAudioDuration,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 8.dp),
                fontSize = 18.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { cancelRecording() }) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = stringResource(R.string.delete_voice_message),
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier
                        .size(30.dp)
                        .padding(4.dp)
                )
            }

            IconButton(onClick = { onPauseOrResume() }) {
                Icon(
                    painter = painterResource(
                        id = if (isPaused) R.drawable.play else R.drawable.pause
                    ),
                    contentDescription = stringResource(R.string.delete_voice_message),
                    tint = ErrorRed,
                    modifier = Modifier
                        .size(36.dp)
                        .padding(4.dp)
                )
            }

            Card(
                onClick = { onSendAudio() },
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.size(42.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.forward),
                    contentDescription = stringResource(R.string.delete_voice_message),
                    tint = DarkBlue,
                    modifier = Modifier
                        .padding(12.dp),
//                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@LightPreviewMode
@Composable
private fun PreviewAudioRecordingBar() = AppTheme(darkTheme = false) {
    Column(
        Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.mainBackground)
    ) {
        AudioRecordingBar(
            cancelRecording = {},
            isPaused = true,
            formattedAudioDuration = "1:23",
            onPauseOrResume = { },
            onSendAudio = { })
    }
}