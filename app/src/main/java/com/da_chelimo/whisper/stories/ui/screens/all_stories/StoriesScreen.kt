package com.da_chelimo.whisper.stories.ui.screens.all_stories

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.SendImage
import com.da_chelimo.whisper.core.presentation.ui.components.AppBottomBar
import com.da_chelimo.whisper.core.presentation.ui.components.BottomBars
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.navigateSafely
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.stories.ui.components.StoryBar
import com.da_chelimo.whisper.stories.ui.components.VerticalDraggable
import com.da_chelimo.whisper.stories.ui.screens.view_story.ViewStoryScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun StoriesScreen(navController: NavController, coroutineScope: CoroutineScope) {
    Box(Modifier.fillMaxSize()) {
        val viewModel: StoriesViewModel = koinViewModel()
        val storyToOpen by viewModel.storyToOpen.collectAsState()

        DefaultScreen(
            navController = navController,
            appBarText = stringResource(R.string.stories)
        ) {

            // PS: Current user will always have a value since StoriesScreen can only be accessed from AllChats (only when the user is logged in)
            val currentUser by viewModel.currentUser.collectAsState(initial = null)
            val myStoryPreview by viewModel.myStoryPreview.collectAsState(initial = null)
            val storyPreviews by viewModel.storyPreviews.collectAsState(initial = null)

            val permissionLauncher =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { imageUri ->
                    if (imageUri != null) {
                        navController.navigateSafely(
                            SendImage(
                                imageUri = imageUri.toString(),
                                chatId = null
//                            sendImageIn = SendImageIn.Story
                            )
                        )
                    }
                }


            Box(modifier = Modifier.weight(1f)) {
                Column(Modifier.fillMaxSize()) {

                    StoryBar(
                        authorID = Firebase.auth.uid,
                        authorName = currentUser?.name,
                        authorProfilePic = currentUser?.profilePic,
                        lastStoryUploadedInMillis = myStoryPreview?.timeUploaded,
                        storyPreviewImage = myStoryPreview?.previewImage,
                        storyCount = myStoryPreview?.storyCount ?: 0,
                        createOrViewStory = {
                            Timber.d("currentUser?.uid == Firebase.auth.uid is ${currentUser?.uid == Firebase.auth.uid}")
                            Timber.d("myStoryPreview?.storyCount is ${myStoryPreview?.storyCount}")
                            if (currentUser?.uid == Firebase.auth.uid && myStoryPreview?.storyCount == 0 || myStoryPreview?.storyCount == null)
                                viewModel.updateOpenMediaPicker(shouldOpen = true)
                            else
                                currentUser?.uid?.let { viewModel.openStoryUsingAuthorID(it) }
                        }
                    )

                    Text(
                        text = stringResource(R.string.recent_stories),
                        modifier = Modifier.padding(top = 12.dp)
                    )

                    if (storyPreviews != null) {
                        LazyColumn(modifier = Modifier.padding(top = 2.dp)) {
                            items(storyPreviews!!) { preview ->
                                StoryBar(
                                    authorID = preview.authorID,
                                    authorName = preview.authorName,
                                    authorProfilePic = preview.authorProfilePic,
                                    lastStoryUploadedInMillis = preview.timeUploaded,
                                    storyPreviewImage = preview.previewImage,
                                    storyCount = preview.storyCount,
                                    createOrViewStory = {
                                        viewModel.openStoryUsingAuthorID(preview.authorID)
                                    }
                                )
                            }
                        }
                    } else {
                        Column(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.no_active_stories),
                                fontFamily = Poppins,
                                fontSize = 17.sp
                            )
                        }
                    }
                }
            }

            val shouldOpenMediaPicker by viewModel.openMediaPicker.collectAsState()
            LaunchedEffect(key1 = shouldOpenMediaPicker) {
                if (shouldOpenMediaPicker) {
                    permissionLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    viewModel.updateOpenMediaPicker(false)
                }
            }


            AppBottomBar(
                currentBottomBar = BottomBars.Stories,
                navController = navController,
                hasPrimaryAction = true,
                onPrimaryAction = {
                    viewModel.updateOpenMediaPicker(shouldOpen = true)
                }
            )
        }

        if (storyToOpen != null) {
            VerticalDraggable(
                coroutineScope = coroutineScope,
                modifier = Modifier.fillMaxSize(),
                onHideStory = { viewModel.resetOpenStory() },
                content = {
                    // TODO: Clear the previous user story data...
                    // For a second, you can see the previous user name and story
                    ViewStoryScreen(
                        authorID = storyToOpen!!,
//                        coroutineScope = coroutineScope,
                        onHideStory = {
                            viewModel.resetOpenStory()
                        }
                    )
                }
            )
        }
    }
}


@Preview
@Composable
private fun PreviewStoriesScreen() = AppTheme {
    StoriesScreen(
        navController = rememberNavController(),
        coroutineScope = rememberCoroutineScope()
    )
}