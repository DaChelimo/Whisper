package com.da_chelimo.whisper.core.repo.user

import android.net.Uri
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.domain.User
import com.google.firebase.firestore.DocumentReference

interface UserRepo {

    companion object {
        const val USERS_COLLECTION = "users"
        const val USER_PROFILE = "profile"

        const val PROFILE_PIC = "profilePic"
    }

    /**
     * Creates the user
     */
    suspend fun createUser(name: String, phoneNumber: String, profilePicLocalUri: Uri?, onComplete: (TaskState) -> Unit)


    /**
     * Provides the Firebase DocumentReference for the user profile based on the user's UID
     */
    fun getUserProfileReference(uid: String): DocumentReference


    /**
     * Fetches a user based on their UID
     *
     * @param uid - UID of the user
     */
    suspend fun getUserFromUID(uid: String): User?


}