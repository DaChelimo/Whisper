package com.da_chelimo.whisper.core.di

import com.da_chelimo.whisper.chats.presentation.actual_chat.screens.ActualChatViewModel
import com.da_chelimo.whisper.chats.repo.contacts.ContactsRepo
import com.da_chelimo.whisper.chats.repo.contacts.ContactsRepoImpl
import com.da_chelimo.whisper.chats.presentation.select_contact.screens.SelectContactsViewModel
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


// For ViewModels and commonly used instances e.g Moshi for serialization
val appModule = module {

    single<Moshi> { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }


    single<ContactsRepo> { ContactsRepoImpl(get(), UserRepoImpl()) }
    viewModel { SelectContactsViewModel(get()) }
    viewModel { ActualChatViewModel(contactsRepo = get()) }
}