package com.da_chelimo.whisper.core.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.theme.GreyBlue
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
fun TintedAppBarIcon(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier
            .size(34.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(6.dp))
            .background(GreyBlue)
            .padding(5.dp),
        tint = MaterialTheme.colorScheme.onSurface
    )
}