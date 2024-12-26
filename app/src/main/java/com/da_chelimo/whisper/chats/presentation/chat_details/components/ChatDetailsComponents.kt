package com.da_chelimo.whisper.chats.presentation.chat_details.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.components.CardPopup
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.DisabledRipple
import com.da_chelimo.whisper.core.presentation.ui.theme.ErrorRed
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand


@Composable
fun ChatDetailActions(
    modifier: Modifier = Modifier,
    onMessage: () -> Unit,
    onAudioCall: () -> Unit,
    onVideoCall: () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 8.dp, vertical = 20.dp)
        ) {
            ChatDetailAction(
                modifier = Modifier.weight(1f),
                actionIcon = R.drawable.messages,
                actionName = stringResource(R.string.message),
                onClick = onMessage
            )
            ChatDetailAction(
                modifier = Modifier.weight(1f),
                actionIcon = R.drawable.phone_call,
                actionName = stringResource(R.string.audio),
                onClick = onAudioCall
            )
            ChatDetailAction(
                modifier = Modifier.weight(1f),
                actionIcon = R.drawable.video_camera,
                actionName = stringResource(R.string.video),
                onClick = onVideoCall
            )
        }
    }
}

@Composable
fun ChatDetailAction(
    @DrawableRes actionIcon: Int,
    actionName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val roundShape = RoundedCornerShape(percent = 16)
    Column(
        modifier = modifier
            .clip(roundShape)
            .clickable { onClick() }
            .border(2.dp, DarkBlue, roundShape)
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(id = actionIcon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = LocalAppColors.current.appThemeTextColor
        )

        Text(
            text = actionName,
            modifier = Modifier.padding(top = 5.dp),
            fontFamily = QuickSand,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = LocalAppColors.current.plainTextColorOnMainBackground
        )

    }
}


@Preview
@Composable
private fun PreviewChatDetailsActions() = AppTheme {
    Column(
        Modifier
            .fillMaxWidth()
            .background(LocalAppColors.current.mainBackground)) {
        ChatDetailActions(Modifier, {}, {}, {})
    }
}


@Composable
fun ComingSoonPopup(modifier: Modifier = Modifier, hidePopup: () -> Unit) {
    CompositionLocalProvider(LocalRippleTheme provides DisabledRipple) {
        CardPopup(modifier = modifier, hidePopup = hidePopup) {

            //  To close any opened keyboard when the popup is shown
            val focusManager = LocalFocusManager.current
            LaunchedEffect(key1 = Unit) {
                focusManager.clearFocus()
            }


            Column(
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.work_icon),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(DarkBlue),
                )

                Text(
                    text = stringResource(R.string.coming_soon),
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontFamily = QuickSand,
                    fontSize = 15.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Medium
                )


                Button(
                    onClick = { hidePopup() },
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth(),
                    border = BorderStroke(1.dp, DarkBlue)
                ) {
                    Text(
                        text = stringResource(R.string.close),
                        fontFamily = QuickSand,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewComingSoonPopup() = AppTheme {
    Column(
        Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.mainBackground)
    ) {
        ComingSoonPopup {

        }
    }
}


@Composable
fun RedBorderButton(name: String, modifier: Modifier = Modifier, onOptionSelected: () -> Unit) {
    Button(
        onClick = { onOptionSelected() },
        modifier = modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = LocalAppColors.current.mainBackground,
            contentColor = LocalAppColors.current.appThemeTextColor
        ),
        border = BorderStroke(1.dp, ErrorRed)
    ) {
        Text(
            text = name,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            fontFamily = QuickSand,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}