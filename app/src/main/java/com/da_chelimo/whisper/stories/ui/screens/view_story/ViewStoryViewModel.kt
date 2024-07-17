package com.da_chelimo.whisper.stories.ui.screens.view_story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.core.domain.User
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.stories.domain.Story
import com.da_chelimo.whisper.stories.repo.StoryRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewStoryViewModel(
    private val userRepo: UserRepo,
    private val storyRepo: StoryRepo
) : ViewModel() {

    private val _typedMessage = MutableStateFlow<String>("")
    val typedMessage: StateFlow<String> = _typedMessage

    private val _author = MutableStateFlow<User?>(null)
    val author: StateFlow<User?> = _author

    private val _stories = MutableStateFlow<List<Story>?>(null)
    val stories: StateFlow<List<Story>?> = _stories

    private val _storyIndex = MutableStateFlow(0)
    val storyIndex: StateFlow<Int> = _storyIndex

    /**
     * If the user has reached the end of the author's stories, hide the story view
     */
    private val _hideStory = MutableStateFlow(false)
    val hideStory: StateFlow<Boolean> = _hideStory



    fun loadUser(authorID: String) = viewModelScope.launch {
        _author.value = userRepo.getUserFromUID(authorID)
    }

    fun loadStories(authorID: String) = viewModelScope.launch {
        _stories.value = storyRepo.loadStories(authorID)
    }


    fun moveToNextStory(currentIndex: Int) {
        val newIndex = currentIndex + 1
        val lastIndex = stories.value?.lastIndex ?: return

        if (newIndex > lastIndex)
            _hideStory.value = true
        else {
            _storyIndex.value = newIndex
        }
    }

    fun moveToPreviousStory(currentIndex: Int) {
        val newIndex = currentIndex - 1

        if (newIndex < 0)
            _hideStory.value = true
        else {
            _storyIndex.value = newIndex
        }
    }


    fun updateTypedMessage(newText: String) {
        _typedMessage.value = newText
    }
    fun resetHideStory() {
        _storyIndex.value = 0
        _hideStory.value = false
    }
}