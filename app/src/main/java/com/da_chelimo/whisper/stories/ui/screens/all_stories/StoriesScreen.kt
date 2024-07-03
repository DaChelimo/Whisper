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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.SendImage
import com.da_chelimo.whisper.core.presentation.ui.SendImageIn
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.navigateSafely
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.stories.ui.components.StoryBar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.compose.koinViewModel

@Composable
fun StoriesScreen(navController: NavController) {
    DefaultScreen(navController = navController, appBarText = stringResource(R.string.stories)) {

        val viewModel: StoriesViewModel = koinViewModel()
        val storyToOpen by viewModel.storyToOpen.collectAsState()

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
                            sendImageIn = SendImageIn.Story
//                            onSendImage = { imageCaption ->
//                                viewModel.postStory(
//                                    imageUri = imageUri.toString(),
//                                    storyCaption = imageCaption
//                                )
//                            }
                        )
                    )
                }
            }


        LaunchedEffect(key1 = storyToOpen) {
            if (storyToOpen != null) {
                navController
                viewModel.resetOpenStory()
            }
        }


        Box(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {

                StoryBar(
                    authorID = currentUser?.uid,
                    authorName = currentUser?.name,
                    authorProfilePic = currentUser?.profilePic,
                    lastStoryUploadedInMillis = myStoryPreview?.timeUploaded,
                    storyPreviewImage = myStoryPreview?.previewImage,
                    storyCount = myStoryPreview?.storyCount ?: 0,
                    createOrViewStory = {
                        if (currentUser?.uid == Firebase.auth.uid && myStoryPreview?.storyCount == 0)
                            viewModel.updateOpenMediaPicker(shouldOpen = true)
                        else
                            currentUser?.uid?.let { viewModel.openStory(it) }
//                    openStory(preview.authorID)
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
                                    viewModel.openStory(preview.authorID)
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

            FloatingActionButton(
                onClick = { viewModel.updateOpenMediaPicker(shouldOpen = true) },
                containerColor = DarkBlue,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_to_story)
                )
            }
        }

        val shouldOpenMediaPicker by viewModel.openMediaPicker.collectAsState()
        LaunchedEffect(key1 = shouldOpenMediaPicker) {
            if (shouldOpenMediaPicker)
                permissionLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

            viewModel.updateOpenMediaPicker(false)
        }
    }

}