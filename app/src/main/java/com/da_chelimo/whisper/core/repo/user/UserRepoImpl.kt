package com.da_chelimo.whisper.core.repo.user

import android.net.Uri
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.domain.User
import com.da_chelimo.whisper.core.repo.user.UserRepo.Companion.PROFILE_PIC
import com.da_chelimo.whisper.core.repo.user.UserRepo.Companion.USERS_COLLECTION
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class UserRepoImpl : UserRepo {

    override suspend fun createUser(
        name: String,
        phoneNumber: String,
        profilePicLocalUri: Uri?,
        onComplete: (TaskState) -> Unit
    ) {
        var profilePicRemoteUrl: Uri? = null

        if (profilePicLocalUri != null)
            profilePicRemoteUrl = Firebase.storage
                .getReference("${Firebase.auth.uid}/$PROFILE_PIC").putFile(profilePicLocalUri)
                .await()
                .storage.downloadUrl.await()

        val user = User(
            uid = Firebase.auth.uid
                ?: throw NullPointerException("Firebase.auth.uid is ${Firebase.auth.uid}"),
            name = name,
            bio = "Da Chelimo is awesome",
            number = phoneNumber,
            profilePic = profilePicRemoteUrl?.toString()
        )

        getUserProfileReference(Firebase.auth.uid!!)
            .set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    onComplete(TaskState.DONE.SUCCESS)
                else
                    onComplete(TaskState.DONE.ERROR(R.string.error_occurred))

            }
    }


    override fun getUserProfileReference(uid: String): DocumentReference =
        Firebase.firestore
            .collection(USERS_COLLECTION)
            .document(uid)


    override suspend fun getUserFromUID(uid: String): User? =
        getUserProfileReference(uid)
            .get().await()
            .toObject<User>()

}