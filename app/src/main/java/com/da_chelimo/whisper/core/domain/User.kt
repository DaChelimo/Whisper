package com.da_chelimo.whisper.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class User(
    val name: String,
    val uid: String,
    val bio: String,
    val profilePic: String?,
    val number: String
) : Parcelable {

    constructor() : this("", "", "", null, "")


    companion object {
        val TEST_User_SHORT_BIO = User(
            "Samantha Chelimo",
            "12345",
            "In another world, in another dimension",
            null,
            "+254794940110"
        )

        val TEST_User_LONG_BIO = User(
            "Adana Chelimo",
            "12345",
            "In another world, in another dimension. I was yours. You were mine",
            null,
            "+254794940110"
        )
    }
}

fun User.toMiniUser() =
    MiniUser(name, uid, profilePic)