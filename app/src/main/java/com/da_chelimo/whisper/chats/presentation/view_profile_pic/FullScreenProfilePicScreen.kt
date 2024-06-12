package com.da_chelimo.whisper.chats.presentation.view_profile_pic

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.skydoves.cloudy.Cloudy
import com.skydoves.cloudy.CloudyState
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Istg I don't even understand how this API works coz putting keys works in
 * some scenarios, and doesn't in others
 * Like what the hellllll....
 *
 * But this is way better than Have {which didn't even add up at first}
 * Well, the end justifies the means (in the meantime... I wanna contribute
 * to Cloudy pretty soon)
 */
@Composable
fun ControlBlurOnScreen(
    isPictureOnFullScreen: Boolean,
    profilePic: String?,
    addKeys: Boolean,
    dismissPicture: () -> Unit,
    content: @Composable (BoxScope) -> Unit
) {
    Box(Modifier.fillMaxSize()) {
//        if (addKeys)
        if (isPictureOnFullScreen) {
            val coroutineScope = rememberCoroutineScope()
            var state by remember { mutableStateOf(0) }
            LaunchedEffect(key1 = Unit) {
                coroutineScope.launch {
//                    delay(500)
                    state = 1
                }
            }
            Cloudy(
                radius = if (isPictureOnFullScreen) 25 else 0,
                key1 = state,
//                key2 = profilePic,
                onStateChanged = {
                    Timber.d("CloudyState is Success: ${it is CloudyState.Success}")
                    Timber.d("CloudyState is Loading: ${it is CloudyState.Loading}")
                    Timber.d("CloudyState is Error: ${it is CloudyState.Error}")
                }
            ) {
                content(this)
            }
        } else
            content(this)
//        else
//            Cloudy(
//                radius = if (isPictureOnFullScreen) 25 else 0,
//                key1 = profilePic,
//                key2 = isPictureOnFullScreen
//            ) {
//                content(this)
//            }

        if (profilePic != null && isPictureOnFullScreen) {
            FullScreenProfilePicScreen(
                profilePic = profilePic,
                modifier = Modifier.align(Alignment.Center),
                dismissProfilePic = dismissPicture
            )
        }
    }

    BackHandler(enabled = isPictureOnFullScreen) {
        dismissPicture()
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FullScreenProfilePicScreen(
    profilePic: String,
    modifier: Modifier = Modifier,
    dismissProfilePic: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = { dismissProfilePic() }
            )
    ) {
        GlideImage(
            model = profilePic,
            contentDescription = stringResource(R.string.change_profile_picture),
            modifier = modifier
                .size(275.dp)
                .clip(CircleShape)
                .clickable { },
            failure = placeholder(R.drawable.alien),
            contentScale = ContentScale.Crop
        )
    }
}


@Preview
@Composable
private fun PreviewFullScreenProfilePicScreen() = AppTheme {
    FullScreenProfilePicScreen("", Modifier, {})
}