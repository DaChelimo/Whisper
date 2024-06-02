package com.da_chelimo.generate.core.data.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.dsl.module


// For ViewModels and commonly used instances e.g Moshi for serialization
val appModule = module {

    single<Moshi> { Moshi.Builder().add(KotlinJsonAdapterFactory()).build() }

}