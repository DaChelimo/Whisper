package com.da_chelimo.whisper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.da_chelimo.whisper.auth.ui.screens.WelcomeScreen
import com.da_chelimo.whisper.auth.ui.screens.create_profile.CreateProfileScreen
import com.da_chelimo.whisper.auth.ui.screens.enter_code.EnterCodeScreen
import com.da_chelimo.whisper.auth.ui.screens.enter_number.EnterNumberScreen
import com.da_chelimo.whisper.chats.presentation.actual_chat.screens.ActualChatScreen
import com.da_chelimo.whisper.chats.presentation.all_chats.screens.AllChatsScreen
import com.da_chelimo.whisper.chats.presentation.chat_details.screens.ChatDetailsScreen
import com.da_chelimo.whisper.chats.presentation.start_chat.screens.SelectContactScreen
import com.da_chelimo.whisper.core.presentation.ui.ActualChat
import com.da_chelimo.whisper.core.presentation.ui.AllChats
import com.da_chelimo.whisper.core.presentation.ui.ChatDetails
import com.da_chelimo.whisper.core.presentation.ui.CreateProfile
import com.da_chelimo.whisper.core.presentation.ui.EnterCode
import com.da_chelimo.whisper.core.presentation.ui.EnterNumber
import com.da_chelimo.whisper.core.presentation.ui.MyProfile
import com.da_chelimo.whisper.core.presentation.ui.SelectContact
import com.da_chelimo.whisper.core.presentation.ui.Settings
import com.da_chelimo.whisper.core.presentation.ui.Welcome
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.settings.presentation.screens.profile.ProfileScreen
import com.da_chelimo.whisper.settings.presentation.screens.settings.SettingsScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val snackbarHostState = SnackbarHostState()

                val coroutineScope = rememberCoroutineScope()
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->

                    Column(Modifier.padding(innerPadding)) {

                        NavHost(
                            navController = navController,
                            startDestination = if (Firebase.auth.uid == null) Welcome else AllChats,
//                            enterTransition = { EnterTransition.None },
//                            exitTransition = { ExitTransition.None }
                        ) {

                            composable<Welcome> {
                                WelcomeScreen(navController = navController)
                            }

                            composable<EnterNumber> {
                                EnterNumberScreen(navController)
                            }

                            composable<EnterCode> {
                                val args = it.toRoute<EnterCode>()

                                EnterCodeScreen(
                                    navController = navController,
                                    phoneNumberWithCountryCode = args.phoneNumberWithCountryCode
                                )
                            }

                            composable<CreateProfile> {
                                val args = it.toRoute<CreateProfile>()

                                CreateProfileScreen(
                                    navController = navController,
                                    snackbarHostState = snackbarHostState,
                                    phoneNumber = args.phoneNumber
                                )
                            }


                            composable<SelectContact> {
                                SelectContactScreen(
                                    context = LocalContext.current,
                                    navController = navController
                                )
                            }


                            composable<Settings> {
                                SettingsScreen(navController = navController)
                            }
                            composable<MyProfile> {
                                ProfileScreen(navController = navController)
                            }


                            composable<AllChats> {
                                AllChatsScreen(
                                    navController = navController,
                                    snackbarHostState = snackbarHostState,
                                    coroutineScope = coroutineScope
                                )
                            }

                            composable<ActualChat> {
                                val args = it.toRoute<ActualChat>()

                                ActualChatScreen(
                                    navController = navController,
                                    chatID = args.chatId,
                                    newContact = args.newContact
                                )
                            }


                            composable<ChatDetails> {
                                val args = it.toRoute<ChatDetails>()

                                ChatDetailsScreen(
                                    chatID = args.chatId,
                                    otherUserID = args.otherUserId,
                                    navController = navController
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}
