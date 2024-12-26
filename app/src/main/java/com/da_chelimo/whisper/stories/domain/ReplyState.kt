package com.da_chelimo.whisper.stories.domain

enum class ReplyState {
    None,
    OpenKeyboard,
    Sending,
    Success,
    ErrorOccurred
}

fun ReplyState.message() = when(this) {
    ReplyState.None -> null
    ReplyState.OpenKeyboard -> null
    ReplyState.Sending -> "Sending story reply..."
    ReplyState.Success -> "Reply sent successfully"
    ReplyState.ErrorOccurred -> "Error occurred"
}