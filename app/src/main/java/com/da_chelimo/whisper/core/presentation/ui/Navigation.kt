package com.da_chelimo.whisper.core.presentation.ui

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object Welcome

@Serializable
object EnterNumber


@Serializable
data class EnterCode(val phoneNumberWithCountryCode: String)


@Serializable
data class CreateProfile(val phoneNumber: String)