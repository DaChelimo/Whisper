package com.da_chelimo.whisper.auth.ui.screens

sealed class TaskState {
    // No task happening
    data object NONE: TaskState()

    data object LOADING: TaskState()

    sealed class DONE: TaskState() {
        data object SUCCESS: DONE()

        data object ERROR: DONE()
    }
}