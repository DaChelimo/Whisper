package com.da_chelimo.whisper.core.presentation.ui.components

import androidx.annotation.FloatRange
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CardPopup(
    modifier: Modifier = Modifier,
    @FloatRange(from = 0.0, to = 1.0) width:  Float = 0.6f,
    hidePopup: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(null, null, onClick = { hidePopup() }),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier
                .fillMaxWidth(width)
                .clickable { },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {

            content()

        }
    }
}