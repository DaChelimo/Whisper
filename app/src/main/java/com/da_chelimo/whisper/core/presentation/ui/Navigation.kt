package com.da_chelimo.whisper.core.presentation.ui

import android.os.Parcelable
import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.da_chelimo.whisper.R
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


/**
 * Learned this from JetSnack....
 *
 * This prevents duplication of navigation events
 */
fun NavBackStackEntry?.isLifecycleResumed() =
    this?.lifecycle?.currentState in listOf(Lifecycle.State.STARTED, Lifecycle.State.RESUMED)

@MainThread
fun <T : Any> NavController.navigateSafely(route: T) {
//    if (currentBackStackEntry.isLifecycleResumed()) {
    navigate(route)
//    }
}

@MainThread
fun <T : Any, R : Any> NavController.navigateSafelyAndPopTo(
    route: T,
    popTo: R,
    isInclusive: Boolean = true
) {
    navigate(route) {
        popUpTo(popTo) {
            inclusive = isInclusive
        }
    }
}


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

@Serializable
data class ViewImage(val imageUrl: String)