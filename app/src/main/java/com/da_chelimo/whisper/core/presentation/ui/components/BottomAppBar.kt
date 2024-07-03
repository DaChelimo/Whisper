package com.da_chelimo.whisper.core.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.AllChats
import com.da_chelimo.whisper.core.presentation.ui.Stories
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand

enum class BottomBars {
    AllChats, Stories, // TODO: Calls
}


@Composable
fun AppBottomBar(currentBottomBar: BottomBars, navController: NavController, modifier: Modifier = Modifier) {
    BottomAppBar(
        modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(50.dp)
    ) {
        val isAllChatScreen = remember { currentBottomBar == BottomBars.AllChats }

        BottomAppBarItem(
            isActive = currentBottomBar == BottomBars.AllChats,
            icon = painterResource(id = R.drawable.start_chat),
            content = stringResource(R.string.chats),
            onClick = {
                if (!isAllChatScreen)
                    navController.navigate(AllChats)
            }
        )


        BottomAppBarItem(
            isActive = currentBottomBar == BottomBars.Stories,
            icon = painterResource(id = R.drawable.stories),
            content = stringResource(R.string.stories),
            onClick = {
                if (isAllChatScreen)
                    navController.navigate(Stories)
            }
        )
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
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.clickable { onClick() }) {
        Icon(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(if (isActive) 32.dp else 28.dp),
            tint = Color.White
        )

        Text(
            text = content,
            modifier = Modifier.padding(top = 4.dp),
            fontFamily = QuickSand,
            fontWeight = if (isActive) FontWeight.Medium else FontWeight.Normal,
            fontSize = if (isActive) 17.sp else 15.sp,
            color = Color.White
        )
    }
}