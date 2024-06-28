package com.da_chelimo.whisper.core.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserIcon(
    profilePic: String?,
    iconSize: Dp,
    progressBarSize: Dp,
    progressBarThickness: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderIfUsingDefaultPic: Dp
) {
    GlideSubcomposition(
        model = profilePic ?: R.drawable.young_man_anim,
        modifier = modifier
            .size(iconSize)
            .clip(CircleShape)
            .clickable { onClick() }
    ) {
        when (state) {
            RequestState.Loading -> {
                Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(progressBarSize),
                        strokeWidth = progressBarThickness,
                        color = LocalAppColors.current.plainTextColorOnMainBackground
                    )
                }
            }

            RequestState.Failure -> {
                Image(
                    imageVector = Icons.Rounded.Person,// painterResource(id = R.drawable.young_man_anim),
                    contentDescription = null,
                    modifier
                        .fillMaxSize()
//                        .size(iconSize)
                        .clip(CircleShape)
                        .border(borderIfUsingDefaultPic, DarkBlue, CircleShape)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(LocalAppColors.current.appThemeTextColor)
                )
            }

            is RequestState.Success -> {
                Image(
                    painter = painter,
                    contentDescription = stringResource(id = R.string.change_profile_picture),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(iconSize)
                        .clip(CircleShape)
                )
            }
        }
    }
}