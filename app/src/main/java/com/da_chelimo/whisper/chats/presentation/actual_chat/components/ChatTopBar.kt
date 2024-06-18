package com.da_chelimo.whisper.chats.presentation.actual_chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand

@Composable
fun ChatTopBar(
    onBackPress: () -> Unit,
    otherPersonName: String?,
    modifier: Modifier = Modifier,
    onVoiceCall: () -> Unit,
    onVideoCall: () -> Unit
) {
    val roundedCornerShape = RoundedCornerShape(0.dp, 0.dp, 6.dp, 6.dp)

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = roundedCornerShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.CenterStart),
                onClick = onBackPress
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.back_button),
                    modifier = Modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = otherPersonName ?: "",
                fontFamily = QuickSand,
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center)
            )

            Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                IconButton(onClick = { onVoiceCall() }) {
                    Icon(
                        imageVector = Icons.Rounded.Call,
                        contentDescription = stringResource(id = R.string.back_button),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                IconButton(onClick = { onVideoCall() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.video_camera),
                        contentDescription = stringResource(id = R.string.back_button),
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewChatTopBar() = AppTheme {
    val navController = rememberNavController()

    Column(Modifier.background(Color.White)) {
        ChatTopBar(
            onBackPress = { navController.popBackStack() },
            otherPersonName = "Andrew Chelimo",
            onVoiceCall = {},
            onVideoCall = {})
    }
}