package com.da_chelimo.whisper.settings.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.components.CardPopup
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand
import com.da_chelimo.whisper.core.presentation.ui.theme.Roboto

@Composable
fun AreYouSurePopup(modifier: Modifier = Modifier, dismissPopup: () -> Unit, deleteAccount: () -> Unit) {
    CardPopup(
        width = 0.75f,
        hidePopup = { dismissPopup() }
    ) {
        Column(
            modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.are_you_sure_you_want_to_delete_your_account),
                fontFamily = QuickSand,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 17.sp
            )
            Text(
                text = stringResource(R.string.you_will_lose_all_your_existing_chats),
                fontFamily = Roboto,
                fontSize = 15.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Row(
            Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 4.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { dismissPopup() },
                colors = ButtonDefaults.buttonColors(contentColor = DarkBlue),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.no))
            }
            OutlinedButton(
                onClick = { deleteAccount() },
                colors = ButtonDefaults.buttonColors(contentColor = DarkBlue),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(R.string.yes))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAreYouSurePopup() = AppTheme {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        AreYouSurePopup(Modifier, {}, {})
    }
}