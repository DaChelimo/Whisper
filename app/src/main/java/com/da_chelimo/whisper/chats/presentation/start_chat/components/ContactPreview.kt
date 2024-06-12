package com.da_chelimo.whisper.chats.presentation.start_chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.core.domain.User
import com.da_chelimo.whisper.core.presentation.ui.components.UserIcon
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand

@Composable
fun ContactPreview(
    contact: User,
    modifier: Modifier = Modifier,
    openProfilePic: () -> Unit,
    startConversation: () -> Unit
) {
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                startConversation()
            }
            .padding(vertical = 8.dp)
            .padding(horizontal = 8.dp)
    ) {

        UserIcon(
            profilePic = contact.profilePic,
            iconSize = 48.dp,
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = {
                openProfilePic()
            }
        )

        Column(modifier = Modifier
            .padding(start = 8.dp, top = 3.dp)
            .align(Alignment.Top)) {
            Text(
                text = contact.name,
                fontFamily = QuickSand,
                fontSize = 15.sp,
                lineHeight = 17.sp,
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = contact.bio,
                fontFamily = Poppins,
                fontSize = 12.sp,
                lineHeight = 15.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f),
                modifier = Modifier
                    .padding(top = 1.dp)
            )
        }

    }
}


@Preview
@Composable
private fun PreviewContactPreview() = AppTheme {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        ContactPreview(
            contact = User.TEST_User_LONG_BIO,
            openProfilePic = {},
            startConversation = {}
        )

        Spacer(Modifier.height(2.dp))

        ContactPreview(
            contact = User.TEST_User_SHORT_BIO,
            openProfilePic = {},
            startConversation = {}
        )
    }
}