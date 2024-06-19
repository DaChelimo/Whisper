package com.da_chelimo.whisper.auth.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.Cabin
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors

@Composable
fun OTPTextField(
    value: String,
    length: Int,
    modifier: Modifier = Modifier,
    spacedBy: Dp = 6.dp,
    boxAlignment: Alignment = Alignment.Center,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.NumberPassword,
        showKeyboardOnFocus = true
    ),
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
                // Prevents highlighting background color in case user chooses to select all
                CompositionLocalProvider(
                    LocalTextSelectionColors.provides(
                        TextSelectionColors(
                            Color.Transparent,
                            Color.Transparent
                        )
                    )
                ) {

                    Row(
                        modifier = Modifier.align(boxAlignment),
                        horizontalArrangement = Arrangement.spacedBy(spacedBy)
                    ) {
                        repeat(length) { index ->
                            val isFocused = index == value.length
                            val char = value.getOrNull(index)?.toString()

                            OTPDigit(char = char, isFocused = isFocused)
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun OTPDigit(char: String?, isFocused: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .border(
                BorderStroke(
                    if (isFocused) 2.dp else 1.dp,
                    if (isFocused)
                        LocalAppColors.current.blueCardColor
                    else MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                ),
                RoundedCornerShape(10.dp)
            )
            .size(40.dp)
    ) {
        if (char != null) {
            Text(
                text = char,
                modifier = Modifier.align(Alignment.Center),
                fontFamily = Cabin,
                fontWeight = FontWeight.SemiBold,
                color = LocalAppColors.current.plainTextColorOnMainBackground
            )
        }
    }
}

@Preview
@Composable
private fun PreviewOTPDigit() = AppTheme {
    var code by remember {
        mutableStateOf("")
    }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        OTPTextField(value = code, length = 6) {
            code = it
        }
    }
//    Row(
//        Modifier
//            .fillMaxWidth()
//            .padding(vertical = 12.dp)
//            .background(MaterialTheme.colorScheme.background),
//        horizontalArrangement = Arrangement.spacedBy(4.dp)
//    ) {
//        OTPDigit("1", false)
//        OTPDigit("2", false)
//        OTPDigit("", true)
//    }
}

@Preview
@Composable
fun OTPTextFieldPreview() = AppTheme {
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