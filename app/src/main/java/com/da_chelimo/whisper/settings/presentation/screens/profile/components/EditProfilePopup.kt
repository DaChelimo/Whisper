package com.da_chelimo.whisper.settings.presentation.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.presentation.ui.theme.AppTheme
import com.da_chelimo.whisper.core.presentation.ui.theme.Cabin
import com.da_chelimo.whisper.core.presentation.ui.theme.DarkBlue
import com.da_chelimo.whisper.core.presentation.ui.theme.LocalAppColors
import com.da_chelimo.whisper.core.presentation.ui.theme.QuickSand

@Composable
fun EditProfilePopup(
    popupTitle: String,
    popupData: String,
    modifier: Modifier = Modifier,
    hidePopup: () -> Unit,
    saveNewChange: (String) -> Unit
) {
    val focusRequester = remember {
        FocusRequester()
    }

    Popup(
        alignment = Alignment.BottomCenter,
        properties = PopupProperties(focusable = true),
        onDismissRequest = { hidePopup() }) {
        Column(
            modifier
                .fillMaxWidth()
                .background(LocalAppColors.current.mainBackground)
                .padding(12.dp)
                .imePadding()
        ) {

            Text(text = popupTitle, modifier = Modifier, fontFamily = Cabin)



            var textFieldValue by remember {
                mutableStateOf(TextFieldValue(popupData, selection = TextRange(index = popupData.length)))
            }
//            var value by remember { mutableStateOf(popupData) }
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,

                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedContainerColor = MaterialTheme.colorScheme.background,

                    unfocusedIndicatorColor = DarkBlue,
                    focusedIndicatorColor = DarkBlue,

                    cursorColor = DarkBlue,
                    selectionColors = TextSelectionColors(DarkBlue, DarkBlue.copy(alpha = 0.5f))
                ),
                textStyle = TextStyle(
                    fontFamily = Cabin,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                ),
                keyboardActions = KeyboardActions {
                    hidePopup()
                    saveNewChange(textFieldValue.text)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    showKeyboardOnFocus = null,
                    imeAction = ImeAction.Done
                )
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            ) {
                Button(onClick = { hidePopup() }) {
                    Text(
                        text = stringResource(R.string.cancel),
                        fontSize = 14.sp,
                        fontFamily = QuickSand,
                        color = LocalAppColors.current.appThemeTextColor,
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(onClick = {
                    hidePopup()
                    saveNewChange(textFieldValue.text)
                }) {
                    Text(
                        text = stringResource(R.string.save),
                        fontSize = 14.sp,
                        fontFamily = QuickSand,
                        color = LocalAppColors.current.appThemeTextColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }
}


@Preview
@Composable
private fun PreviewEditProfilePopup() = AppTheme(darkTheme = true) {
    Column(
        Modifier
            .fillMaxSize()
            .background(LocalAppColors.current.mainBackground)
    ) {
        EditProfilePopup(
            popupTitle = stringResource(id = R.string.enter_your_name),
            popupData = "Andrew",
            hidePopup = { },
            saveNewChange = {})
    }
}

