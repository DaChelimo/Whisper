package com.da_chelimo.whisper.settings.presentation.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.Cabin
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand

@Composable
fun ProfileItem(
    startIcon: ImageVector,
    title: String,
    data: String,
    textStyle: TextStyle,
    onEditClicked: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Icon(
            imageVector = startIcon, contentDescription = null, modifier = Modifier
                .padding(top = 4.dp)
                .size(30.dp)
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground.copy(0.8f),
                fontFamily = Cabin,
                lineHeight = 14.sp,
                fontSize = 14.sp
            )

            Text(
                text = data,
                modifier = Modifier
                    .padding(end = 8.dp),
                fontFamily = QuickSand,
                style = textStyle,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }


        if (onEditClicked != null) {
            Icon(
                imageVector = Icons.Outlined.Create,
                contentDescription = null,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .padding(2.dp)
                    .align(Alignment.CenterVertically)
                    .clickable { onEditClicked() },
                tint = MaterialTheme.colorScheme.surface
            )
        }
    }
}


@Preview
@Composable
private fun PreviewProfileItem() = AppTheme {
    Column(
        Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {

        ProfileItem(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            startIcon = Icons.Outlined.Person,
            title = "Name",
            data = "Da Chelimo",
            textStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.SemiBold),
            onEditClicked = { }
        )

    }
}