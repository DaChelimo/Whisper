package com.da_chelimo.whisper.core.presentation.ui

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


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
object Groups
@Serializable
object Calls

@Serializable
object Stories
@Serializable
data class ViewStory(val authorID: String)


@Serializable
object AllChats

@Serializable
data class ActualChat(val chatId: String?, val newContact: String?)

@Serializable
data class ChatDetails(val chatId: String, val otherUserId: String)


@Parcelize
@Serializable
open class SendImageIn: Parcelable {
    data class Chat(val chatId: String?) : SendImageIn()
//    class Chat(val chatId: String?): SendImageIn()

    data object Story : SendImageIn()
//        private fun readResolve(): Any = Story
//    }
}

inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        bundle.getString(key)?.let<String, T>(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}

inline fun <reified T : Parcelable> parcelableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = json.encodeToString(value)

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)
}

//val SendImageInNavType = object : NavType.SerializableType<SendImageIn>(type = SendImageIn::class.java) {}

@Parcelize
@Serializable
data class SendImage(val imageUri: String, val chatId: String?): Parcelable  //, val onSendImage: (String) -> Unit)
@Serializable
data class ViewImage(val imageUrl: String)
