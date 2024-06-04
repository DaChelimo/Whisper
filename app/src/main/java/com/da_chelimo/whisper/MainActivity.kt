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
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.da_chelimo.whisper.auth.ui.screens.create_profile.CreateProfileScreen
import com.da_chelimo.whisper.auth.ui.screens.enter_code.EnterCodeScreen
import com.da_chelimo.whisper.auth.ui.screens.enter_number.EnterNumberScreen
import com.da_chelimo.whisper.core.presentation.ui.CreateProfile
import com.da_chelimo.whisper.core.presentation.ui.EnterCode
import com.da_chelimo.whisper.core.presentation.ui.EnterNumber
import com.da_chelimo.whisper.core.presentation.ui.Home
import com.da_chelimo.whisper.core.presentation.ui.screens.HomeScreen
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val snackbarHostState = SnackbarHostState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {

                        val navController = rememberNavController()

                        NavHost(navController = navController, startDestination = Home) {

                            // Home Screen
                            composable<Home> {
                                HomeScreen()
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
                        }

                    }
                }
            }
        }
    }
}
