package com.da_chelimo.compose_ccp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.da_chelimo.compose_ccp.model.PickerUtils
import com.da_chelimo.compose_ccp.theme.CorrectGreen
import com.da_chelimo.compose_ccp.theme.DarkBlue
import com.da_chelimo.compose_ccp.theme.ErrorRed
import com.da_chelimo.compose_ccp.theme.QuickSand
import com.da_chelimo.compose_ccp.theme.SelectionBlue
import com.da_chelimo.compose_countrycodepicker.libs.Country


@Composable
fun CCPTextField(
    country: Country,
    number: String,
    isValid: Boolean,
    errorMessage: String?,
    textColor: Color,
    containerColor: Color,
    defaultIndicatorColor: Color,
    onCountryChange: (Country) -> Unit,
    onNumberChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var showDialog by remember {
        mutableStateOf(false)
    }
    val indicatorColor =
        if (isValid) CorrectGreen else defaultIndicatorColor
    val isError =
        !isValid && number.isNotBlank() && number.isNotEmpty() && errorMessage != null

    Row(modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = number,
            onValueChange = { onNumberChange(it) },
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = containerColor,
                focusedContainerColor = containerColor,
                errorContainerColor = containerColor,

                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                errorTextColor = textColor,

                cursorColor = DarkBlue,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.surface,
                    backgroundColor = SelectionBlue.copy(alpha = 0.4f)
                ),

                unfocusedSupportingTextColor = indicatorColor,
                focusedSupportingTextColor = indicatorColor,

                unfocusedIndicatorColor = indicatorColor,
                focusedIndicatorColor = indicatorColor,

                errorIndicatorColor = ErrorRed
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone
            ),
            leadingIcon = {
                Row(
                    Modifier
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null,
                            onClick = { showDialog = true })
                        .padding(start = 8.dp, end = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = country.flag),
                        contentDescription = null,
                        modifier = Modifier
                            .height(24.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = country.phoneNoCode,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 15.sp,
                        fontFamily = QuickSand,
                        color = textColor
                    )
                }
            },
            textStyle = TextStyle.Default.copy(
                fontSize = 15.sp,
                fontFamily = QuickSand,
                fontWeight = FontWeight.Medium,
                letterSpacing = (0.8).sp
            ),
            singleLine = true,
            placeholder = {
                Text(
                    text = "722123456",
                    color = textColor.copy(alpha = 0.7f),
                    fontSize = 15.sp,
                    fontFamily = QuickSand
                )
            },
            isError = isError,
            supportingText = {
                if (isError)
                    Text(errorMessage!!)
            }
        )
    }

// In case the user was putting in their number then halfway, navigated
// to the Country Picker, we need to hide the keyboard
    LaunchedEffect(key1 = showDialog) {
        focusManager.clearFocus()
    }


    if (showDialog)
        CountrySelectCountryDialog(
            modifier = Modifier,
            textColor = textColor,
            containerColor = containerColor,
            indicatorColor = indicatorColor,
            onHideDialog = {
                if (it != null)
                    onCountryChange(it)

                showDialog = false
            }
        )
}


@Preview
@Composable
private fun PreviewCCPTextField() {
    val context = LocalContext.current
    var country by remember { mutableStateOf(PickerUtils.getDefaultCountry(context)) }
    var number by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        CCPTextField(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            country = country,
            number = number,
            isValid = false,
            containerColor = MaterialTheme.colorScheme.background,
            textColor = MaterialTheme.colorScheme.surface,
            defaultIndicatorColor = DarkBlue,
            errorMessage = "Enter valid number",
            onCountryChange = { country = it },
            onNumberChange = { number = it }
        )
    }
}
