package com.da_chelimo.whisper.settings.presentation.screens.settings

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsViewModel: ViewModel() {


    fun signOut() { Firebase.auth.signOut() }

}