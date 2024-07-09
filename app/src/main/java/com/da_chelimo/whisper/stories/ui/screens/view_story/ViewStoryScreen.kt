package com.da_chelimo.whisper.stories.ui.screens.view_story

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.components.Glider
import com.da_chelimo.whisper.core.presentation.ui.components.UserIcon
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.LightBlack
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.stories.domain.StoryPreview
import com.da_chelimo.whisper.stories.ui.components.StoryTopCountIndicator
import com.da_chelimo.whisper.stories.ui.components.ViewStoryMessageBar
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewStoryScreen(authorID: String, onHideStory: () -> Unit) {
    val viewModel: ViewStoryViewModel = koinViewModel()
    val author by viewModel.author.collectAsState(initial = null)

    val stories by viewModel.stories.collectAsState(initial = null)
    val storyIndex by viewModel.storyIndex.collectAsState()
    val hideStory by viewModel.hideStory.collectAsState()

    val typedMessage by viewModel.typedMessage.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.loadUser(authorID)
        viewModel.loadStories(authorID)
    }

    // TODO: Change this to automatically go to the next person's story
    LaunchedEffect(key1 = hideStory) {
        if (hideStory) {
            onHideStory()
            viewModel.resetHideStory()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
    ) {

        if (stories != null) {
            val pagerState = rememberPagerState { stories!!.size }
            val localConfiguration = LocalConfiguration.current


            LaunchedEffect(key1 = storyIndex) {
                if (storyIndex != pagerState.currentPage)
                    pagerState.animateScrollToPage(storyIndex)
            }


            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 4.dp)
            ) { storyIndex ->
                Column(
                    Modifier
                        .fillMaxHeight()
                        .combinedClickable(
                            onDoubleClick = {
                                // TODO: Like story
                            },
                            onClick = {}
                        )
                        .pointerInput(stories) {
                            detectTapGestures { offset ->
                                val centerX = (localConfiguration.screenWidthDp.dp / 2)
                                val tapPosition = offset.x.toDp()

                                Timber.d("IS tap on the right: ${tapPosition > centerX}")
                                if (tapPosition > centerX)
                                    viewModel.moveToNextStory(storyIndex)
                                else
                                    viewModel.moveToPreviousStory(storyIndex)
                            }
                        }
                ) {
                    Glider(
                        imageUrl = stories?.getOrNull(storyIndex)?.imageUrl,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(LightBlack),
                        loading = {
                            CircularProgressIndicator(
                                Modifier.size(48.dp),
                                strokeWidth = 4.dp,
                                trackColor = LocalAppColors.current.appThemeTextColor
                            )
                        },
                        error = {
                            Image(
                                painter = painterResource(id = R.drawable.no_users_on_whisper),
                                contentDescription = null,
                                modifier = Modifier.size(150.dp)
                            )

                            Text(
                                text = stringResource(id = R.string.error_occurred),
                                Modifier.padding(top = 8.dp),
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    )

                    Column(
                        Modifier
                            .height(90.dp)
                            .background(DarkBlue)
                    ) {
                        ViewStoryMessageBar(
                            text = typedMessage,
                            onTextChange = { viewModel.updateTypedMessage(typedMessage) },
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            Column {
                StoryTopCountIndicator(
                    currentStoryIndex = pagerState.currentPage,
                    totalStoryCount = pagerState.pageCount,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .padding(horizontal = 4.dp)
                )

                Row(
                    modifier = Modifier.padding(top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UserIcon(
                        profilePic = author?.profilePic,
                        iconSize = 52.dp,
                        modifier = Modifier.padding(4.dp),
                        onClick = { }
                    )

                    Text(
                        text = author?.name ?: "",
                        fontFamily = QuickSand,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        modifier = Modifier.padding(start = 4.dp),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = {
                        // TODO: Open story options
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = stringResource(R.string.open_story_options),
                            modifier = Modifier.size(28.dp),
                            tint = Color.White
                        )
                    }
                }
            }

        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(LightBlack),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp), strokeWidth = 4.dp)
            }
        }
    }
}


@Preview
@Composable
private fun PreviewViewStoryScreen() = AppTheme {
    ViewStoryScreen(authorID = StoryPreview.TEST.authorID) {}
}