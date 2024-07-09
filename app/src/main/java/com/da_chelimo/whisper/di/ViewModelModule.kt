package com.da_chelimo.whisper.di

import com.da_chelimo.whisper.chats.presentation.actual_chat.screens.ActualChatViewModel
import com.da_chelimo.whisper.chats.presentation.select_contact.screens.SelectContactsViewModel
import com.da_chelimo.whisper.stories.ui.screens.all_stories.StoriesViewModel
import com.da_chelimo.whisper.stories.ui.screens.view_story.ViewStoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {  SelectContactsViewModel(get()) }

    viewModel { ActualChatViewModel(contactsRepo = get()) }

    viewModel { StoriesViewModel(get(), get(), get()) }
    viewModel { ViewStoryViewModel(get(), get()) }
}