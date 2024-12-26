package com.da_chelimo.whisper.stories.ui.screens.view_story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.core.domain.User
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.stories.domain.ReplyState
import com.da_chelimo.whisper.stories.domain.Story
import com.da_chelimo.whisper.stories.repo.StoryRepo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewStoryViewModel(
    private val userRepo: UserRepo,
    private val storyRepo: StoryRepo
) : ViewModel() {

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


    private val _replyState = MutableStateFlow(ReplyState.None)
    val replyState: StateFlow<ReplyState> = _replyState



    fun loadUser(authorID: String) = viewModelScope.launch {
        _author.value = userRepo.getUserFromUID(authorID)
    }

    fun loadStories(authorID: String) = viewModelScope.launch {
        _stories.value = storyRepo.loadStories(authorID)
    }

    fun deleteStory() = viewModelScope.launch {
        val story = stories.value?.getOrNull(storyIndex.value) ?: return@launch
        _stories.value = stories.value?.filter { it.storyID != story.storyID }
        storyRepo.deleteStory(story.storyID)
    }

    fun moveToNextStory() {
        val newIndex = storyIndex.value + 1
        val lastIndex = stories.value?.lastIndex ?: return

        if (newIndex > lastIndex)
            _hideStory.value = true
        else {
            _storyIndex.value = newIndex
        }
    }

    fun moveToPreviousStory() {
        val newIndex = storyIndex.value - 1

        if (newIndex < 0)
            _hideStory.value = true
        else {
            _storyIndex.value = newIndex
        }
    }

    fun openKeyboard() { _replyState.value = ReplyState.OpenKeyboard }
    fun hideKeyboard() { _replyState.value = ReplyState.None }


    fun sendStoryReply(reply: String) = viewModelScope.launch {
        val currentStory = stories.value?.getOrNull(storyIndex.value) ?: return@launch
        _replyState.value = ReplyState.Sending
        delay(1500)
        _replyState.value = ReplyState.Success
    }


    fun resetReplyState() {
        _replyState.value = ReplyState.None
    }

    fun resetHideStory() {
        _storyIndex.value = 0
        _hideStory.value = false
    }
}