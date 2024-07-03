package com.da_chelimo.whisper.di

import com.da_chelimo.whisper.chats.repo.chats.ChatRepo
import com.da_chelimo.whisper.chats.repo.chats.ChatRepoImpl
import com.da_chelimo.whisper.chats.repo.contacts.ContactsRepo
import com.da_chelimo.whisper.chats.repo.contacts.ContactsRepoImpl
import com.da_chelimo.whisper.chats.repo.messages.MessagesRepo
import com.da_chelimo.whisper.chats.repo.messages.MessagesRepoImpl
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.da_chelimo.whisper.core.repo.user_details.UserDetailsRepo
import com.da_chelimo.whisper.core.repo.user_details.UserDetailsRepoImpl
import com.da_chelimo.whisper.stories.repo.StoryRepo
import com.da_chelimo.whisper.stories.repo.StoryRepoImpl
import org.koin.dsl.module


val repoModule = module {

    // Users
    single<UserRepo> { UserRepoImpl() }
    single<UserDetailsRepo> { UserDetailsRepoImpl() }
    single<ContactsRepo> { ContactsRepoImpl(get(), get()) }

    // Chats
    single<ChatRepo> { ChatRepoImpl(get()) }
    single<MessagesRepo> { MessagesRepoImpl(get()) }


    // Story
    single<StoryRepo> { StoryRepoImpl(get(), get()) }

}