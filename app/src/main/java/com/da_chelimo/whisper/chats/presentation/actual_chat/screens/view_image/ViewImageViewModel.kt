package com.da_chelimo.whisper.chats.presentation.actual_chat.screens.view_image

import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.joda.time.DateTime
import java.io.File

class ViewImageViewModel : ViewModel() {

    fun downloadImage(imageUrl: String, context: Context) = viewModelScope.launch {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val metadata = Firebase.storage.getReferenceFromUrl(imageUrl).metadata.await()


        val jodaTime = DateTime(metadata.creationTimeMillis).toLocalDateTime()
        val fileName = "IMG-${jodaTime.year}${jodaTime.monthOfYear}${jodaTime.dayOfMonth}-Whisper${jodaTime.millisOfDay}"
        val imageURI = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "$fileName.jpg").toUri()


        val request = Request(Uri.parse(imageUrl)).setDestinationUri(imageURI).setTitle(fileName).setNotificationVisibility(Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        downloadManager.enqueue(request)
    }

}