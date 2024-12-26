package com.da_chelimo.whisper.core.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand

@Composable
fun AppSnackbar(showSnackbar: Boolean, message: String?, modifier: Modifier = Modifier) {
    AnimatedVisibility(visible = showSnackbar) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            elevation = CardDefaults.elevatedCardElevation(),
            colors = CardDefaults.cardColors(
                containerColor = DarkBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                .padding(horizontal = 8.dp)
                .heightIn(min = 52.dp),
                verticalArrangement = Arrangement.Center
            ) {
                message?.let { message ->
                    Text(
                        text = message,
                        fontFamily = QuickSand,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}