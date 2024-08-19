package com.da_chelimo.whisper.stories.ui.screens.view_story

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.components.Glider
import com.da_chelimo.whisper.core.presentation.ui.components.UserIcon
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.DisabledRipple
import com.da_chelimo.whisper.core.presentation.ui.theme.LightBlack
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.stories.domain.StoryPreview
import com.da_chelimo.whisper.stories.ui.components.StoryTopCountIndicator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun ViewStoryScreen(authorID: String, onHideStory: () -> Unit) {
    val viewModel: ViewStoryViewModel = koinViewModel()
    val author by viewModel.author.collectAsState(initial = null)
    val isCurrentUser = author?.uid == Firebase.auth.uid
    val coroutineScope = rememberCoroutineScope()

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

            var isPaused by remember {
                mutableStateOf(false)
            }
            /**
             * Every time we move to a new story, remove Pausing
             */
            LaunchedEffect(key1 = storyIndex) {
                isPaused = false
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 4.dp)
            ) { storyIndex ->
                val story = remember { stories?.getOrNull(storyIndex) }


                var pressJob: Job? = remember {
                    null
                }
                Column(
                    Modifier
                        .fillMaxHeight()
                        .pointerInput(stories) {
                            detectTapGestures(
                                onPress = {
                                    coroutineScope.launch {
                                        pressJob?.cancel()
                                        pressJob = launch {
                                            delay(250)
                                            isPaused = true
                                            awaitRelease()
                                            isPaused = false
                                        }
                                    }
                                },
                                onTap = { offset ->
                                    /**
                                     * Ensures that this is a tap and not a long press
                                     */
                                    if (!isPaused) {
                                        Timber.d("onTap called")
                                        val centerX = (localConfiguration.screenWidthDp.dp / 2)
                                        val tapPosition = offset.x.toDp()

                                        Timber.d("IS tap on the right: ${tapPosition > centerX}")
                                        if (tapPosition > centerX)
                                            viewModel.moveToNextStory()
                                        else
                                            viewModel.moveToPreviousStory()
                                    }
                                }
                            )
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(LightBlack)
                    ) {
                        Glider(
                            imageUrl = story?.imageUrl,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 8.dp),
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

                        story?.storyCaption?.let { caption ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.2f))
                                    .align(Alignment.BottomCenter),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = caption,
                                    fontSize = 17.sp,
                                    lineHeight = 18.sp,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(vertical = 18.dp)
                                        .padding(horizontal = 12.dp),
                                    textAlign = TextAlign.Center
                                )

                                Button(
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                    onClick = { },
                                    contentPadding = PaddingValues(
                                        horizontal = 16.dp,
                                        vertical = 2.dp
                                    ),
                                    shape = CircleShape,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                    border = BorderStroke(
                                        (0.5).dp,
                                        LocalAppColors.current.plainTextColorOnMainBackground
                                    )
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.reply),
                                        fontFamily = QuickSand,
                                        fontWeight = FontWeight.SemiBold,
                                        color = LocalAppColors.current.plainTextColorOnMainBackground
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.reply),
                                        modifier = Modifier.padding(start = 4.dp),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }

            var showStoryOption by remember {
                mutableStateOf(false)
            }
            Column {
                StoryTopCountIndicator(
                    isPaused = isPaused,
                    currentStoryIndex = pagerState.currentPage,
                    totalStoryCount = pagerState.pageCount,
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .padding(horizontal = 4.dp),
                    onTimerOver = {
                        viewModel.moveToNextStory()
                    }
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
                        showStoryOption = true
                        isPaused = true
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = stringResource(R.string.open_story_options),
                            modifier = Modifier.size(28.dp),
                            tint = Color.White
                        )
                    }
                }

                AnimatedVisibility(visible = showStoryOption) {
                    CompositionLocalProvider(value = LocalRippleTheme provides DisabledRipple) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxSize()
                                .clickable {
                                    showStoryOption = false
                                    isPaused = false
                                }
                        ) {
                            Card(
                                Modifier
                                    .fillMaxWidth(0.4f)
                                    .padding(end = 12.dp)
                                    .align(Alignment.TopEnd),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = LocalAppColors.current.mainBackground)
                            ) {
                                Column(Modifier.padding(vertical = 4.dp).clickable {
                                    /**
                                     * If it is the current user's story, allow story deletion
                                     * Otherwise, allow muting only
                                     */
                                    if (isCurrentUser) {
                                        isPaused = false
                                        viewModel.deleteStory()
                                    } else {
                                        showStoryOption = false
                                    }
                                }) {
                                    Text(
                                        text = if (isCurrentUser) "Delete" else "Mute",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp, horizontal = 8.dp)
                                    )
                                }
                            }
                        }
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