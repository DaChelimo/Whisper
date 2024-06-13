package com.da_chelimo.whisper.core.presentation.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
object Welcome

@Serializable
object EnterNumber

@Serializable
object SelectContact


@Serializable
data class EnterCode(val phoneNumberWithCountryCode: String)


@Serializable
@Parcelize
data class CreateProfile(val phoneNumber: String) : Parcelable


@Serializable
object Settings

@Serializable
object MyProfile




@Serializable
object AllChats
@Serializable
data class ActualChat(val chatId: String?, val newContact: String?)
@Serializable
data class ChatDetails(val chatId: String, val otherUserId: String)
@Serializable
data class SendImage(val chatId: String, val imageUri: String)
