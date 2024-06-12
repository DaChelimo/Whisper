package com.da_chelimo.whisper.core.repo.user_details

import android.net.Uri
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.domain.User
import kotlinx.coroutines.flow.Flow

interface UserDetailsRepo {

    val getUserProfileFlow: Flow<User?>

    /**
     * Updates the user's name in the main /user/ DB
     * then fetches all the personal chat IDS,
     * then changes the user name in all the chats
     *
     * @return isSuccessful - true if operation is a success
     */
    suspend fun updateUserName(userID: String, newName: String): Boolean


    suspend fun updateUserBio(userID: String, newBio: String): Boolean


    /**
     * Updates the user's profilePic in the main /user/ DB
     * then fetches all the personal chat IDS,
     * then changes the user profilePic in all the chats
     *
     * @return A callback flow which sends progress on the image upload progress
     */
   suspend fun updateUserProfilePic(userID: String, newProfilePicLocalUri: Uri): Flow<TaskState>

}