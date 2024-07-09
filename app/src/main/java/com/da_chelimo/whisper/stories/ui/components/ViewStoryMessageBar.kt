package com.da_chelimo.whisper.stories.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.LightGrey
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand

@Composable
fun ViewStoryMessageBar(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        Surface(
            modifier = modifier.weight(1f).padding(start = 8.dp),
            shape = RoundedCornerShape(60),
            border = BorderStroke((0.5).dp, Color.White)
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle.Default.copy(
                    fontSize = 14.sp,
                    fontFamily = QuickSand,
                    fontWeight = FontWeight.Medium
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    unfocusedPlaceholderColor = LightGrey,
                    focusedPlaceholderColor = LightGrey
                ),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.send_message),
                        style = TextStyle.Default.copy(
                            fontSize = 14.sp,
                            fontFamily = QuickSand,
                            fontWeight = FontWeight.Medium
                        ),
                    )
                }
            )
        }

        TextButton(
            onClick = { },
            modifier = Modifier
                .padding(end = 8.dp, start = 4.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(R.string.send),
                color = if (text.isEmpty()) LightGrey else DarkBlue,
                fontSize = 15.sp,
                fontFamily = QuickSand,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
private fun PreviewViewStoryMessageBar() = AppTheme {
    var message by remember { mutableStateOf("") }

    Column(
        Modifier
            .height(100.dp)
            .background(DarkBlue)
    ) {
        ViewStoryMessageBar(
            text = message,
            onTextChange = { message = it },
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}