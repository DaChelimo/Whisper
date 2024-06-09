package com.da_chelimo.whisper.core.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme

@Composable
fun DefaultScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    appBarText: String? = null,
    surfaceColor: Color = MaterialTheme.colorScheme.surface,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable ColumnScope.() -> Unit
) {
    DefaultScreen(
        navController = navController,
        modifier = modifier,
        appBar = {
            AppBar(navController = navController, appBarText = appBarText)
        },
        surfaceColor = surfaceColor,
        backgroundColor = backgroundColor,
        content = content
    )
}

@Composable
fun DefaultScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    appBar: @Composable () -> Unit,
    surfaceColor: Color = MaterialTheme.colorScheme.surface,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor)
    ) {
        val roundedCornerShape = RoundedCornerShape(
            topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp
        )

        appBar()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 4.dp)
                .clip(roundedCornerShape)
                .background(backgroundColor)
                .then(modifier)
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun PreviewDefaultScreen() {
    val navController = rememberNavController()

    AppTheme {
        DefaultScreen(navController = navController, appBarText = "Register") {

        }
    }
}