package com.da_chelimo.whisper.core.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.da_chelimo.whisper.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserIcon(profilePic: String?, iconSize: Dp, onClick: () -> Unit, modifier: Modifier = Modifier) {
    if (profilePic != null)
        GlideImage(
            model = profilePic,
            contentDescription = stringResource(R.string.change_profile_picture),
            modifier = modifier
                .size(iconSize)
                .clip(CircleShape),
            failure = placeholder(R.drawable.alien),
            contentScale = ContentScale.Crop
        )
    else
        Image(
            painter = painterResource(id = R.drawable.alien),
            contentDescription = null,
            modifier = modifier
                .size(iconSize)
                .clip(CircleShape)
                .clickable {
                    onClick()
                },
            contentScale = ContentScale.Crop
        )
}