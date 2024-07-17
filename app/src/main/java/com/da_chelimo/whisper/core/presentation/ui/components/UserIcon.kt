package com.da_chelimo.whisper.core.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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

@Composable
fun UserIcon(
    profilePic: String?,
    modifier: Modifier = Modifier,
    iconSize: Dp = 48.dp,
    progressBarSize: Dp = 22.dp,
    progressBarThickness: Dp = (1.5).dp,
    borderIfUsingDefaultPic: Dp? = null,
    onClick: () -> Unit
) {
    Glider(
        contentScale = ContentScale.Crop,
        imageUrl = profilePic ?: R.drawable.young_man_anim,
        contentDescription = stringResource(id = R.string.change_profile_picture),

        modifier = modifier
            .size(iconSize)
            .clip(CircleShape)
            .clickable { onClick() },

        loading = {
            CircularProgressIndicator(
                modifier = Modifier.size(progressBarSize),
                strokeWidth = progressBarThickness,
                color = LocalAppColors.current.plainTextColorOnMainBackground
            )
        },
        error = {
            Image(
                imageVector = Icons.Rounded.Person,// painterResource(id = R.drawable.young_man_anim),
                contentDescription = null,
                modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .apply {
                        borderIfUsingDefaultPic?.let { border(it, DarkBlue, CircleShape) }
                    }
                    .padding(8.dp),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(LocalAppColors.current.appThemeTextColor)
            )
        }
    )
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Glider(
    imageUrl: Any?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    loading: @Composable ColumnScope.() -> Unit,
    error: @Composable ColumnScope.() -> Unit,
    contentScale: ContentScale,
) {
    GlideSubcomposition(model = imageUrl, modifier = modifier) {
        when (state) {
            is RequestState.Success -> {
                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                    Image(
                        painter = painter,
                        contentDescription = contentDescription,
                        contentScale = contentScale,
                        modifier = modifier
                    )
                }
            }

            is RequestState.Loading -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    loading()
                }
            }

            is RequestState.Failure -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    error()
                }
            }
        }
    }
}