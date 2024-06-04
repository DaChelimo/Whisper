package com.da_chelimo.whisper.auth.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand

@Composable
fun LoadingSpinner(modifier: Modifier = Modifier) {
    Card(
        modifier.fillMaxWidth(0.38f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 32.dp)
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.surface,
                trackColor = MaterialTheme.colorScheme.background,
                modifier = Modifier.size(42.dp)
            )

            Text(
                text = stringResource(R.string.loading),
                fontFamily = QuickSand,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}


@Preview
@Composable
private fun PreviewLoadingSpinner() = AppTheme {
    Column(Modifier.fillMaxSize()) {
        LoadingSpinner()
    }
}