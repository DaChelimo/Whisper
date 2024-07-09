package com.da_chelimo.whisper.groups.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.chats.presentation.chat_details.components.ComingSoonPopup
import com.da_chelimo.whisper.core.presentation.ui.AllChats
import com.da_chelimo.whisper.core.presentation.ui.components.AppBottomBar
import com.da_chelimo.whisper.core.presentation.ui.components.BottomBars
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.navigateSafelyAndPopTo

@Composable
fun GroupsScreen(navController: NavController) {
    var hideDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = hideDialog) {
        if (hideDialog)
            navController.navigateSafelyAndPopTo(
                route = AllChats,
                popTo = AllChats,
                isInclusive = false
            )
    }

    DefaultScreen(
        navController = navController,
        appBarText = stringResource(id = R.string.groups)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ComingSoonPopup(
                modifier = Modifier.align(Alignment.Center),
                hidePopup = { hideDialog = true }
            )

            AppBottomBar(
                currentBottomBar = BottomBars.Groups,
                navController = navController,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}