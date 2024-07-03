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
): ViewModel() {

    private val _author = MutableStateFlow<User?>(null)
    val author: StateFlow<User?> = _author

    private val _stories = MutableStateFlow<List<Story>?>(null)
    val stories: StateFlow<List<Story>?> = _stories

    fun loadUser(authorID: String) = viewModelScope.launch {
        _author.value = userRepo.getUserFromUID(authorID)
    }

    fun loadStories(authorID: String) = viewModelScope.launch {
        _stories.value = storyRepo.loadStories(authorID)
    }

}