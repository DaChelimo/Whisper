package com.da_chelimo.whisper.chats.presentation.actual_chat.screens.send_image

import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo
import com.da_chelimo.whisper.chats.repo.chats.ChatRepoImpl
import com.da_chelimo.whisper.chats.repo.messages.MessagesRepo
import com.da_chelimo.whisper.chats.repo.messages.MessagesRepoImpl
import com.da_chelimo.whisper.core.domain.TaskState
import com.da_chelimo.whisper.core.presentation.ui.SendImageIn
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.core.repo.user.UserRepoImpl
import com.da_chelimo.whisper.stories.repo.StoryRepo
import com.da_chelimo.whisper.stories.repo.StoryRepoImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SendImageViewModel(
    private val messagesRepo: MessagesRepo = MessagesRepoImpl(ChatRepoImpl()),
    private val userRepo: UserRepo = UserRepoImpl(),
    private val chatRepo: ChatRepo = ChatRepoImpl(),
    private val storyRepo: StoryRepo = StoryRepoImpl(userRepo, chatRepo)
): ViewModel() {

    private val _typedMessage = MutableStateFlow(TextFieldValue(""))
    val typedMessage: StateFlow<TextFieldValue> = _typedMessage

    private val _sendImageState = MutableStateFlow<TaskState>(TaskState.NONE)
    val sendImageState: StateFlow<TaskState> = _sendImageState

    fun updateMessage(newMessage: TextFieldValue) {
        _typedMessage.value = newMessage
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun sendMessage(imageUri: String, sendImageIn: SendImageIn) = viewModelScope.launch {
        if (sendImageState.value is TaskState.LOADING) return@launch

        _sendImageState.value = TaskState.LOADING()

//        onSendImage: suspend () -> Unit
//        onSendImage()
        /**
         * Send image in the chat
         */
        if (sendImageIn is SendImageIn.Chat) {
            GlobalScope.launch {
                sendImageIn.chatId?.let { messagesRepo.sendImageMessage(it, imageUri, typedMessage.value.text) }
            }
            delay(1200)
        }
        /**
         * Post the image on the user's story
         */
        else {
            Firebase.auth.uid?.let { userID ->
                storyRepo.postStory(
                    localImageUri = imageUri.toUri(),
                    storyCaption = typedMessage.value.text,
                    currentUserID = userID
                )
            }
        }


        _sendImageState.value = TaskState.DONE.SUCCESS
    }


    fun resetSendState() {
        _sendImageState.value = TaskState.NONE
    }
}