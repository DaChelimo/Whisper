@file:OptIn(ExperimentalGlideComposeApi::class)

package com.da_chelimo.whisper.chats.presentation.actual_chat.screens.send_image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.chats.presentation.actual_chat.components.TypeMessageBar
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.presentation.ui.components.LoadingSpinner

@Composable
fun SendImageScreen(navController: NavController, chatID: String, imageUri: String) {
    val viewModel = viewModel<SendImageViewModel>()
    val typedMessage by viewModel.typedMessage.collectAsState()

    val focusManager = LocalFocusManager.current
    val sendImageState by viewModel.sendImageState.collectAsState()


    LaunchedEffect(key1 = sendImageState) {
        if (sendImageState is TaskState.DONE.SUCCESS) {
            navController.popBackStack()
            viewModel.resetSendState()
        } else if (sendImageState is TaskState.LOADING) {
            focusManager.clearFocus(force = true)
        }
    }

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                    viewModel.resetSendState()
                },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.back_button),
                    modifier = Modifier.size(32.dp)
                )
            }


            Column(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                GlideImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                    requestBuilderTransform = {
                        it.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    }
                )
            }

            TypeMessageBar(
                modifier = Modifier,
                value = typedMessage,
                onValueChange = { viewModel.updateMessage(it) },
                openMediaSelector = null,
                startAudioRecording = null,
                sendMessage = {
                    viewModel.sendMessage(chatID, imageUri)
                }
            )
        }

        if (sendImageState is TaskState.LOADING)
            LoadingSpinner(modifier = Modifier.align(Alignment.Center))
    }
}