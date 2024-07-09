package com.da_chelimo.whisper.stories.ui.screens.all_stories

import androidx.lifecycle.ViewModel
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user_details.UserDetailsRepo
import com.da_chelimo.whisper.stories.repo.StoryRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class StoriesViewModel(
    private val userRepo: UserRepo,
    private val storyRepo: StoryRepo,
    private val userDetailsRepo: UserDetailsRepo
): ViewModel() {

    val currentUser = userDetailsRepo.getUserProfileFlow

    val myStoryPreview = storyRepo.getMyStoryPreview()
    val storyPreviews = storyRepo.getStoryPreviews()

    private val _openMediaPicker = MutableStateFlow(false)
    val openMediaPicker: StateFlow<Boolean> = _openMediaPicker

    private val _storyToOpen = MutableStateFlow<String?>(null)
    val storyToOpen: StateFlow<String?> = _storyToOpen


//    fun postStory(imageUri: String, storyCaption: String) = viewModelScope.launch {
//        Firebase.auth.uid?.let { userID ->
//            storyRepo.postStory(
//                localImageUri = imageUri.toUri(),
//                storyCaption = storyCaption,
//                userID = userID
//            )
//        }
//    }


    fun openStoryUsingAuthorID(authorUID: String) {
        _storyToOpen.value = authorUID
    }

    fun resetOpenStory() {
        _storyToOpen.value = null
    }

    fun updateOpenMediaPicker(shouldOpen: Boolean) {
        Timber.d("updateOpenMediaPicker called")
        _openMediaPicker.value = shouldOpen
    }

}