package com.da_chelimo.whisper.core.repo.user_details

import android.net.Uri
import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.presentation.utils.toChatPreviewTime
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.CHAT_DETAILS
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo.Companion.getChatDetailsRef
import com.da_chelimo.whisper.core.domain.MiniUser
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.domain.User
import com.da_chelimo.whisper.core.domain.UserStatus
import com.da_chelimo.whisper.core.domain.isOnline
import com.da_chelimo.whisper.core.domain.toMiniUser
import com.da_chelimo.whisper.core.repo.user.UserRepo.Companion.getStorageRefForProfilePic
import com.da_chelimo.whisper.core.repo.user.UserRepo.Companion.getUserProfileReference
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class UserDetailsRepoImpl : UserDetailsRepo {

    override val getUserProfileFlow: Flow<User?>
        get() = callbackFlow {
            val userListener = getUserProfileReference(Firebase.auth.uid!!)
                .addSnapshotListener { value, error ->
                    val user = value?.toObject(User::class.java)
                    Timber.e(error)

                    trySend(user)
                }


            awaitClose {
                userListener.remove()
            }
        }


    override fun getUserLastSeenAsFlow(user: Flow<MiniUser?>): Flow<String?> = callbackFlow {
        var lastSeenCallback: ListenerRegistration? = null

        user.collectLatest {
            val userID = it?.uid

            if (userID != null) {
                lastSeenCallback =
                    getUserProfileReference(userID).addSnapshotListener { value, error ->
                        error?.printStackTrace()

                        val updatedUser = value?.toObject<User>()
//                        Timber.d("updatedUser is $updatedUser")

                        trySend(
                            if (updatedUser != null) {
                                if (updatedUser.isOnline()) User.ONLINE
                                else "Last Seen: ${
                                    updatedUser.lastSeen.toChatPreviewTime(addAmPMSymbol = true)
                                }"
                            } else
                                null
                        )
                    }
            }
        }

        awaitClose {
            lastSeenCallback?.remove()
        }
    }


    override suspend fun goOnline(userID: String) {
        Timber.d("Go Online called")
        getUserProfileReference(userID).update(
            User::userStatus.name, UserStatus.Active
        ).await()
    }

    override suspend fun goOffline(userID: String) {
        Timber.d("Go Offline called")

        getUserProfileReference(userID).update(
            User::userStatus.name, UserStatus.Offline
        ).await()

        Timber.d("GoOffline was successful: true")
    }

    override suspend fun updateUserName(userID: String, newName: String): Boolean {
        val currentUser =
            getUserProfileReference(userID).get().await().toObject(User::class.java)?.toMiniUser()
        val newCurrentUser = currentUser?.copy(name = newName)

        val isSuccessful = getUserProfileReference(userID)
            .update(User::name.name, newName)
            .isSuccessful

        updateUserProfileInExistingChats(currentUser, newCurrentUser)

        return isSuccessful // TODO: Make this more accurate since it should reflect the success of all tasks, not the first one only
    }

    override suspend fun updateUserBio(userID: String, newBio: String): Boolean =
        getUserProfileReference(userID)
            .update(User::bio.name, newBio)
            .isSuccessful


    override fun updateUserLastSeen(userID: String, lastSeen: Long) {
//        Timber.d("Service: updateUserLastSeen with lastSeen as $lastSeen")

        getUserProfileReference(userID)
            .update(User::lastSeen.name, lastSeen)

//        if (userStatus == UserStatus.Offline)
//            getUserProfileReference(userID)
//                .update(User::lastSeen.name, System.currentTimeMillis())
    }

    override suspend fun updateUserProfilePic(
        userID: String,
        newProfilePicLocalUri: Uri
    ): Flow<TaskState> = callbackFlow {
        val newProfilePic = getStorageRefForProfilePic(userID).putFile(newProfilePicLocalUri)
            .addOnProgressListener { task ->
                val progress = ((task.bytesTransferred) / (task.totalByteCount)) * 100
                Timber.d("progress is $progress")

                trySend(TaskState.LOADING(progress = progress.toInt()))
            }
            .await()
            .storage.downloadUrl
            .await()
            .toString()

        Timber.d("newProfilePic is $newProfilePic")

        val currentUser =
            getUserProfileReference(userID).get().await().toObject(User::class.java)?.toMiniUser()
        val newCurrentUser = currentUser?.copy(profilePic = newProfilePic)

        // Update the main profile in /users/
        getUserProfileReference(userID)
            .update(User::profilePic.name, newProfilePic)

        // Update the user profile in the chats collection
        updateUserProfileInExistingChats(currentUser, newCurrentUser)

        trySend(TaskState.DONE.SUCCESS)

        awaitClose {
            trySend(TaskState.DONE.SUCCESS)
        }
    }.catch {
        Timber.e(it)
    }


    private suspend fun updateUserProfileInExistingChats(
        currentUser: MiniUser?,
        newCurrentUser: MiniUser?
    ) {
        val chatsWhereCurrentUserIsFirstUser = Firebase.firestore.collection(CHAT_DETAILS)
            .whereEqualTo(Chat::firstMiniUser.name, currentUser)
            .get()
            .await()
            .toObjects(Chat::class.java)
            .map { it.chatID }

        chatsWhereCurrentUserIsFirstUser.forEach { chatId ->
            getChatDetailsRef(chatId)
                .update(Chat::firstMiniUser.name, newCurrentUser)
        }


        val chatsWhereCurrentUserIsSecondUser = Firebase.firestore.collection(CHAT_DETAILS)
            .whereEqualTo(Chat::secondMiniUser.name, currentUser)
            .get()
            .await()
            .toObjects(Chat::class.java)
            .map { it.chatID }

        chatsWhereCurrentUserIsSecondUser.forEach { chatId ->
            getChatDetailsRef(chatId)
                .update(Chat::secondMiniUser.name, newCurrentUser)
        }
    }
}