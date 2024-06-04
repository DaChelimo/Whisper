package com.da_chelimo.whisper.core.domain

import android.net.Uri

data class User(
    val uid: String,
    var name: String,
    val number: String,
    var profilePicture: Uri?,
) {

    constructor(): this("", "", "", null)

}