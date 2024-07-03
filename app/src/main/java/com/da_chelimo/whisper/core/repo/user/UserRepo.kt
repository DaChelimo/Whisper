package com.da_chelimo.whisper.core.repo.user

import android.net.Uri
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.domain.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

interface UserRepo {

    companion object {
        const val USERS_COLLECTION = "users"

        const val PROFILE_PIC = "profilePic"

        fun getStorageRefForProfilePic(uid: String) =
            Firebase.storage
                .getReference("USERS/$uid/$PROFILE_PIC")

        fun getUserProfileReference(uid: String): DocumentReference =
            Firebase.firestore
                .collection(USERS_COLLECTION)
                .document(uid)
    }

    /**
     * Creates the user
     */
    suspend fun createUser(name: String, bio: String, phoneNumber: String, profilePicLocalUri: Uri?, onComplete: (TaskState) -> Unit)



    /**
     * Fetches a user based on their UID
     *
     * @param uid - UID of the user
     */
    suspend fun getUserFromUID(uid: String): User?


    /**
     * Deletes the user's account
     */
    suspend fun deleteUser(uid: String): Boolean
}