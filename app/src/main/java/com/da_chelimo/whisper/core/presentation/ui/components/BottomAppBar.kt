package com.da_chelimo.whisper.core.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.AllChats
import com.da_chelimo.whisper.core.presentation.ui.Calls
import com.da_chelimo.whisper.core.presentation.ui.Groups
import com.da_chelimo.whisper.core.presentation.ui.Stories
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkerBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.LightGrey
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand

enum class BottomBars {
    AllChats, Groups, Stories, Calls
}

// Messages, Groups, Start Chat, Stories, Calls
@Composable
fun AppBottomBar(
    currentBottomBar: BottomBars,
    navController: NavController,
    modifier: Modifier = Modifier,
    hasPrimaryAction: Boolean = false,
    onPrimaryAction: () -> Unit = {}
) {
    val lighterMainBackground = LocalAppColors.current.lighterMainBackground

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(vertical = 12.dp, horizontal = 8.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .height(68.dp)
                .clip(RoundedCornerShape(20))
        ) {
            drawRect(DarkBlue)

            drawCircle(lighterMainBackground, radius = 95f, center = this.center.copy(y = -15f))
        }

        if (hasPrimaryAction) {
            // Show this in the messages screen ONLY
            FloatingActionButton(
                onClick = onPrimaryAction,
                shape = CircleShape,
                containerColor = DarkerBlue,
                contentColor = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(62.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(id = R.string.start_a_chat),
                    Modifier.size(34.dp)
                )
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)
                .padding(horizontal = 4.dp)
                .height(60.dp)
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomAppBarItem(
                isActive = BottomBars.AllChats.isCurrentScreen(currentBottomBar),
                icon = painterResource(id = R.drawable.medium_messages),
                content = stringResource(R.string.chats),
                modifier = Modifier.weight(1f),
                onClick = {
                    if (!BottomBars.AllChats.isCurrentScreen(currentBottomBar))
                        navController.navigate(AllChats)
                }
            )

            BottomAppBarItem(
                isActive = BottomBars.Groups.isCurrentScreen(currentBottomBar),
                icon = painterResource(id = R.drawable.groups),
                content = stringResource(R.string.groups),
                modifier = Modifier.weight(1f),
                onClick = {
                    if (!BottomBars.Groups.isCurrentScreen(currentBottomBar))
                        navController.navigate(Groups)
                }
            )

            Spacer(modifier = Modifier.width(50.dp))

            BottomAppBarItem(
                isActive = BottomBars.Stories.isCurrentScreen(currentBottomBar),
                icon = painterResource(id = R.drawable.stories),
                content = stringResource(R.string.stories),
                modifier = Modifier.weight(1f),
                onClick = {
                    if (!BottomBars.Stories.isCurrentScreen(currentBottomBar))
                        navController.navigate(Stories)
                }
            )

            BottomAppBarItem(
                isActive = BottomBars.Calls.isCurrentScreen(currentBottomBar),
                icon = painterResource(id = R.drawable.med_calls),
                content = stringResource(R.string.calls),
                modifier = Modifier.weight(1f),
                onClick = {
                    if (!BottomBars.Calls.isCurrentScreen(currentBottomBar))
                        navController.navigate(Calls)
                }
            )
        }
    }
}


@Composable
fun BottomAppBarItem(
    isActive: Boolean,
    icon: Painter,
    content: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable { onClick() }
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(22.dp), //(if (isActive) 32.dp else 28.dp),
            tint = if (isActive) Color.White else LightGrey
        )

        Text(
            text = content,
            modifier = Modifier.padding(top = 2.dp),
            fontFamily = QuickSand,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = if (isActive) Color.White else LightGrey
        )
    }
}

fun BottomBars.isCurrentScreen(currentBottomBar: BottomBars) = this == currentBottomBar

@Preview
@Composable
private fun PreviewAppBottomBar() = AppTheme {
    AppBottomBar(currentBottomBar = BottomBars.AllChats, navController = rememberNavController())
}