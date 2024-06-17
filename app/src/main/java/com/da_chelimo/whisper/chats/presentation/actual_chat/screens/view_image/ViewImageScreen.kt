@file:OptIn(ExperimentalGlideComposeApi::class)

package com.da_chelimo.whisper.chats.presentation.actual_chat.screens.view_image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.da_chelimo.whisper.R

@Composable
fun ViewImageScreen(
    navController: NavController,
    imageUrl: String
) {
    val viewModel = viewModel<ViewImageViewModel>()
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.back_button),
                    modifier = Modifier
                        .size(32.dp)
                )
            }

            IconButton(onClick = { viewModel.downloadImage(imageUrl, context) }) {
                Icon(
                    painter = painterResource(id = R.drawable.download),
                    contentDescription = stringResource(R.string.download),
                    modifier = Modifier
                        .size(20.dp)
                )
            }
        }

        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 12.dp)
                .padding(bottom = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .align(Alignment.CenterHorizontally)
        ) {
            GlideImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                requestBuilderTransform = {
                    it.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                }
            )
        }
    }

}