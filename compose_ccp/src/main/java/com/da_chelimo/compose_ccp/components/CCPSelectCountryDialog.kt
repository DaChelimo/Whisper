package com.da_chelimo.compose_ccp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.da_chelimo.compose_ccp.R
import com.da_chelimo.compose_ccp.model.PickerUtils
import com.da_chelimo.compose_ccp.theme.QuickSand
import com.da_chelimo.compose_countrycodepicker.libs.Country


@Composable
fun CountrySelectCountryDialog(
    modifier: Modifier = Modifier,
    titleStyle: TextStyle =
        TextStyle.Default.copy(fontSize = 17.sp,
            fontFamily = QuickSand, fontWeight = FontWeight.SemiBold),
    onHideDialog: (Country?) -> Unit
) {
    var countryName by remember {
        mutableStateOf("")
    }
    val countries =
        PickerUtils.allCountries.filter { it.name.contains(countryName, ignoreCase = true) }


    Dialog(onDismissRequest = { onHideDialog(null) }) {
            Card(
                modifier = modifier
                    .fillMaxHeight(0.75f)
                    .fillMaxWidth(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(bottom = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(Modifier.padding(8.dp)) {
                    Text(
                        text = stringResource(R.string.select_country),
                        modifier = Modifier.padding(top = 8.dp),
                        style = titleStyle
                    )

                    OutlinedTextField(
                        value = countryName,
                        onValueChange = { countryName = it },
                        modifier = Modifier.padding(vertical = 12.dp)
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = MaterialTheme.colorScheme.surface
                        ),
                        textStyle = TextStyle.Default.copy(fontFamily = QuickSand, fontSize = 14.sp, fontWeight = FontWeight.Medium),
                        keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Words),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search_country_name),
                                fontSize = 14.sp,
                                fontFamily = QuickSand,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }
                    )

                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(countries) { country ->
                            Row(
                                Modifier
                                    .clickable { onHideDialog(country) }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = country.flag),
                                    contentDescription = null,
                                    modifier = Modifier.height(26.dp).clip(RoundedCornerShape(4.dp))
                                )

                                Text(
                                    text = country.name,
                                    fontSize = 15.sp,
                                    fontFamily = QuickSand,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                        .weight(1f)
                                )

                                Text(
                                    text = country.phoneNoCode,
                                    fontSize = 15.sp,
                                    fontFamily = QuickSand,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            }
                        }
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewCountrySelectCountryDialog() {
    Column(Modifier.fillMaxSize()) {
        CountrySelectCountryDialog {

        }
    }
}