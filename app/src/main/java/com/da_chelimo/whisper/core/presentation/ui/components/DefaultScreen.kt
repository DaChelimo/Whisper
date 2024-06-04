package com.da_chelimo.whisper.core.presentation.ui.components

import androidx.appcompat.widget.AppCompatSeekBar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand

@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    appBarText: String? = null,
    onBackPressed: () -> Unit = { navController.popBackStack() }
) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onBackPressed() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.back_button),
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        if (appBarText != null) {
            Text(
                text = appBarText,
                fontFamily = QuickSand,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}


@Composable
fun DefaultScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    appBarText: String? = null,
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
            topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 12.dp
        )

        AppBar(navController = navController, appBarText = appBarText)

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