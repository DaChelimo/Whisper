package com.da_chelimo.whisper.core.domain

import androidx.annotation.StringRes

sealed class TaskState {
    // No task happening
    data object NONE: TaskState()

    // Loading: Task in progress
    data class LOADING(val progress: Int? = null): TaskState()

    sealed class DONE: TaskState() {
        data object SUCCESS: DONE()

        data class ERROR(@StringRes val errorMessageRes: Int): DONE()
    }
}