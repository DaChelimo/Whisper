package com.da_chelimo.whisper.core.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkPreviewMode
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors

@Composable
fun DefaultScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    appBarText: String? = null,
    surfaceColor: Color = LocalAppColors.current.blueCardColor,
    backgroundColor: Color = LocalAppColors.current.mainBackground,
    content: @Composable ColumnScope.() -> Unit
) {
    DefaultScreen(
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
    modifier: Modifier = Modifier,
    appBar: @Composable () -> Unit,
    surfaceColor: Color = LocalAppColors.current.blueCardColor,
    backgroundColor: Color = LocalAppColors.current.mainBackground,
    content: @Composable() (ColumnScope.() -> Unit)
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

@DarkPreviewMode
@Composable
private fun PreviewDefaultScreen() {
    val navController = rememberNavController()

    AppTheme {
        DefaultScreen(navController = navController, appBarText = "Register") {

        }
    }
}