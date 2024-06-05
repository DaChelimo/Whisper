package com.da_chelimo.whisper.auth.ui.screens.create_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.R
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.domain.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CreateProfileViewModel : ViewModel() {

    private val _profilePic = MutableStateFlow<Uri?>(null)
    val profilePic: StateFlow<Uri?> = _profilePic

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _taskState = MutableStateFlow<TaskState>(TaskState.NONE)
    val taskState: StateFlow<TaskState> = _taskState

    fun createUserProfile(phoneNumber: String) {
        val profilePicLocalUri = profilePic.value
        var profilePicRemoteUrl: Uri? = null

        viewModelScope.launch {
            _taskState.value = TaskState.LOADING

            if (profilePicLocalUri != null)
                profilePicRemoteUrl = Firebase.storage
                    .getReference("${Firebase.auth.uid}/profilePic").putFile(profilePicLocalUri)
                    .await()
                    .storage.downloadUrl.await()

            val user = User(
                uid = Firebase.auth.uid
                    ?: throw NullPointerException("Firebase.auth.uid is ${Firebase.auth.uid}"),
                name = name.value,
                number = phoneNumber,
                profilePicture = profilePicRemoteUrl
            )

            val uploadTask = Firebase.firestore
                .document("USERS")
                .collection(Firebase.auth.uid!!)
                .document("PROFILE")
                .set(user)

            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful)
                    _taskState.value = TaskState.DONE.SUCCESS
                else
                    _taskState.value = TaskState.DONE.ERROR(R.string.error_occurred)

            }
        }
    }

    fun updateName(newName: String) {
        _name.value = newName
    }

    fun updateProfilePic(newPic: Uri) {
        _profilePic.value = newPic
    }


    fun resetTaskState() {
        _taskState.value = TaskState.NONE
    }

}