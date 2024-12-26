package com.da_chelimo.whisper.settings.repo

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import com.da_chelimo.whisper.core.presentation.ui.theme.Theme
import kotlinx.coroutines.flow.map
import timber.log.Timber


class SettingsDataStore(
    context: Context
) {

    private val datastore = context.createDataStore("settings")
    private val isDarkModeKey = preferencesKey<Boolean>("themeKey")

    val isDarkTheme = getTheme().map { theme ->
        Timber.d("getTheme() is $theme")
        theme?.let { it == Theme.Dark } }

    private fun getTheme() = datastore.data.map {
        val isDarkMode = it[isDarkModeKey]
        isDarkMode?.let { if (isDarkMode) Theme.Light else Theme.Dark }
    }

    suspend fun updateTheme(newTheme: Theme) {
        Timber.d("updateTheme with newTheme is $newTheme")
        datastore.edit { it[isDarkModeKey] = newTheme == Theme.Dark }
    }
}