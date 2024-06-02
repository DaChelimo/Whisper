package com.da_chelimo.generate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.generate.core.presentation.ui.Home
import com.da_chelimo.generate.core.presentation.ui.screens.HomeScreen
import com.da_chelimo.generate.core.presentation.ui.theme.GenerateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            GenerateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {

                        val navController = rememberNavController()

                        NavHost(navController = navController, startDestination = Home) {

                            // Home Screen
                            composable<Home> {
                                HomeScreen()
                            }
                        }

                    }
                }
            }
        }
    }
}
