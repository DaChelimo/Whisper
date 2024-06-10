package com.da_chelimo.compose_countrycodepicker.libs

import androidx.annotation.DrawableRes

data class Country(
    val code: String,
    val phoneNoCode: String,
    val name: String,
    @DrawableRes val flag: Int
)
