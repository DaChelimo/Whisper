package com.da_chelimo.whisper.chats.start_chat.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.auth.ui.components.LoadingSpinner
import com.da_chelimo.whisper.chats.start_chat.components.ContactPreview
import com.da_chelimo.whisper.core.presentation.ui.AllChats
import com.da_chelimo.whisper.core.presentation.ui.components.DefaultScreen
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun SelectContactScreen(
    context: Context,
    navController: NavController,
    viewModel: SelectContactsViewModel = koinViewModel()
) {
    val contactsOnWhisper by viewModel.contactsOnWhisper.collectAsState(initial = listOf())
    val shouldNavigateToActualChat by viewModel.shouldNavigateToActualChat.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchContactsOnWhisper(context)
    }

    LaunchedEffect(key1 = shouldNavigateToActualChat) {
        if (shouldNavigateToActualChat != null) {
            navController.navigate(shouldNavigateToActualChat!!) {
                popUpTo(AllChats) {
                    inclusive = false
                }
            }
            viewModel.resetShouldNavigateToActualChat()
        }
    }


    DefaultScreen(
        navController = navController,
        appBarText = stringResource(R.string.select_contact),
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .padding(top = 8.dp, bottom = 4.dp)
    ) {

        Text(
            text = stringResource(R.string.contacts_on_whisper),
            fontFamily = QuickSand,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        if (contactsOnWhisper.isNotEmpty()) {
            LazyColumn {
                items(contactsOnWhisper) { contact ->
                    ContactPreview(
                        contact = contact,
                        openProfilePic = {
                            // TODO: Open DP preview
                        },
                        startConversation = {
                            Timber.d("Navigating with contact as $contact")
                            viewModel.startOrResumeConversation(contact)
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
                LoadingSpinner()
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSelectContactScreen() {
    SelectContactScreen(
        context = LocalContext.current,
        navController = rememberNavController()
    )
}