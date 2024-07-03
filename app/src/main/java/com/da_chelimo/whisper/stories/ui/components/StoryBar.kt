package com.da_chelimo.whisper.stories.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.components.UserIcon
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.core.utils.toStoryTime
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Composable
fun StoryBar(
    authorID: String?,
    authorProfilePic: String?,
    authorName: String?,
    lastStoryUploadedInMillis: Long?,
    storyPreviewImage: String?,
    storyCount: Int,
    modifier: Modifier = Modifier,
    createOrViewStory: () -> Unit
) {
    val isMyStory = remember { authorID == Firebase.auth.uid }

    Row(modifier = modifier) {
        StoryIcon(
            authorID = authorID,
            storyPreviewImage = storyPreviewImage ?: authorProfilePic,
            myStoryCount = storyCount,
            onClick = { createOrViewStory() })

        Column(Modifier.padding(start = 8.dp)) {
            Text(
                text = if (isMyStory) "My story" else authorName ?: "",
                fontFamily = QuickSand,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp
            )

            // The current user has no story uploaded
            val message =
                if (lastStoryUploadedInMillis == null && isMyStory)
                    stringResource(R.string.create_your_story)
                else lastStoryUploadedInMillis?.toStoryTime()

            if (message != null) {
                Text(
                    text = message,
                    modifier = Modifier.padding(top = 2.dp),
                    fontSize = 13.sp,
                    color = LocalAppColors.current.appThemeTextColor.copy(alpha = 0.75f)
                )
            }
        }
    }
}


@Composable
fun StoryIcon(
    authorID: String?,
    storyPreviewImage: String?,
    modifier: Modifier = Modifier,
    myStoryCount: Int,
    onClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxWidth()) {

        // TODO: If story count is more than one, adjust the border accordingly
        UserIcon(
            profilePic = storyPreviewImage,
            iconSize = 48.dp,
            progressBarSize = 20.dp,
            progressBarThickness = (1.5).dp,
            onClick = { onClick() },
            borderIfUsingDefaultPic = 0.dp
        )

        if (myStoryCount == 0 && authorID == Firebase.auth.uid)
            Icon(
                imageVector = Icons.Rounded.AddCircle,
                contentDescription = null,
                tint = DarkBlue,
                modifier = Modifier
                    .size(20.dp)
                    .background(LocalAppColors.current.lighterMainBackground)
            )
    }
}