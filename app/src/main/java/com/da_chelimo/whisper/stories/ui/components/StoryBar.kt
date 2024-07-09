package com.da_chelimo.whisper.stories.ui.components

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.components.UserIcon
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.LightGrey
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

    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { createOrViewStory() }
        .padding(horizontal = 8.dp, vertical = 10.dp)
    ) {

        StoryIcon(
            isMyStory = isMyStory,
            storyPreviewImage = storyPreviewImage ?: authorProfilePic,
            myStoryCount = storyCount,
            onClick = {
                createOrViewStory()
            }
        )

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


@Preview
@Composable
private fun PreviewStoryBar() = AppTheme {
    StoryBar(
        authorID = "",
        authorProfilePic = null,
        authorName = "Bob",
        lastStoryUploadedInMillis = System.currentTimeMillis(),
        storyPreviewImage = null,
        storyCount = 0,
        createOrViewStory = {}
    )
}

@Composable
fun StoryTopCountIndicator(
    currentStoryIndex: Int,
    totalStoryCount: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier.fillMaxWidth()) {
        repeat(totalStoryCount) { index ->
            val isOrWasViewed = index <= currentStoryIndex
            Surface(
                modifier = Modifier.height((1.5).dp).weight(1f),
                shape = RoundedCornerShape(50),
                color = if (isOrWasViewed) Color.White else LightGrey
            ) {}

            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}


@Composable
fun StoryIcon(
    isMyStory: Boolean,
    storyPreviewImage: String?,
    modifier: Modifier = Modifier,
    myStoryCount: Int,
    onClick: () -> Unit
) {
    Box(modifier = modifier.clickable { onClick() }) {

        // TODO: If story count is more than one, adjust the border accordingly
        UserIcon(
            profilePic = storyPreviewImage,
            iconSize = 52.dp,
            progressBarSize = 20.dp,
            progressBarThickness = (1.5).dp,
            onClick = { onClick() },
            borderIfUsingDefaultPic = 0.dp
        )

        if (myStoryCount == 0 && isMyStory)
            Surface(
                color = DarkBlue, modifier = Modifier
                    .clip(CircleShape)
                    .size(20.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(2.dp)
                )
            }
    }
}