package com.da_chelimo.whisper.auth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors

@Composable
fun CodeField(
    value: String,
    length: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        singleLine = true,
        onValueChange = {
            if (it.length <= length) {
                onValueChange(it)
            }
        },
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Box {
                // hide the inner text field as we are dwelling the text field ourselves
                CompositionLocalProvider(
                    LocalTextSelectionColors.provides(
                        TextSelectionColors(
                            Color.Transparent,
                            Color.Transparent
                        )
                    )
                ) {
                    Box(modifier = Modifier.drawWithContent { }) {
                        innerTextField()
                    }
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    repeat(length) { index ->
                        // if char is not null, show it, otherwise show empty box
                        val currentChar = value.getOrNull(index)
                        Box(
                            modifier = modifier
                                .size(38.dp)
                                .border(
                                    width = 1.dp,
                                    color = if (currentChar != null) LocalAppColors.current.blueCardColor else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(8.dp),
                                )
                        ) {
                            if (currentChar != null) {
                                Text(
                                    modifier = Modifier.align(Alignment.Center),
                                    text = currentChar.toString(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 18.sp,
                                    color = LocalAppColors.current.plainTextColorOnMainBackground
                                )
                            }
                        }

                        if (index != length - 1)
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 2.dp)
                                    .width(8.dp)
                                    .align(Alignment.CenterVertically),
                                text = "-",
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                color = Color.LightGray,
                                fontSize = 32.sp
                            )
                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun CodeTextFieldPreview() = AppTheme {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        var field by remember {
            mutableStateOf("123")
        }
        Row(Modifier.padding(horizontal = 8.dp)) {
            CodeField(
                value = field,
                length = 6,
                onValueChange = {
                    field = it
                }
            )
        }
    }
}