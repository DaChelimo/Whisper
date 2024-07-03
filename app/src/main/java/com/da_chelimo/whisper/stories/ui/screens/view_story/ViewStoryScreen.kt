package com.da_chelimo.whisper.stories.ui.screens.view_story

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.components.Glider
import com.da_chelimo.whisper.core.presentation.ui.components.UserIcon
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.LightBlack
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.Poppins
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.stories.domain.StoryPreview
import org.koin.androidx.compose.koinViewModel

@Composable
fun ViewStoryScreen(navController: NavController, authorID: String) {
    val viewModel: ViewStoryViewModel = koinViewModel()
    val author by viewModel.author.collectAsState(initial = null)
    val stories by viewModel.stories.collectAsState(initial = null)

    LaunchedEffect(key1 = Unit) {
        viewModel.loadUser(authorID)
        viewModel.loadStories(authorID)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (stories != null) {

            LazyRow(modifier = Modifier.fillMaxSize()) {
                items(stories!!) {
                    Glider(
                        imageUrl = author?.profilePic,
                        modifier = Modifier
                            .fillMaxSize()
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

        var isNavigatingBack = remember { false }
        Row(Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                if (!isNavigatingBack) {
                    isNavigatingBack = true
                    navController.popBackStack()
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.back_button),
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            UserIcon(
                profilePic = author?.profilePic,
                iconSize = 52.dp,
                modifier = Modifier.padding(6.dp),
                onClick = { }
            )

            Text(
                text = author?.name ?: "",
                fontFamily = QuickSand,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
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
}


@Preview
@Composable
private fun PreviewViewStoryScreen() = AppTheme {
    ViewStoryScreen(navController = rememberNavController(), authorID = StoryPreview.TEST.authorID)
}