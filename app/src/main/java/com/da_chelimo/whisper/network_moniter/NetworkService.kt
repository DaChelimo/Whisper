package com.da_chelimo.whisper.network_moniter

import android.app.Service
import android.content.Intent
import android.os.IBinder
import timber.log.Timber

//class NetworkService(
//    private val networkMonitor: NetworkMonitor
//) : Service() {
//
//    override fun onBind(intent: Intent?): IBinder? = null
//
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        networkMonitor.setupCallback(applicationContext, false)
//        Timber.d("Service: onStartCommand called")
//
//        return START_STICKY
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Timber.d("Service: onDestroy called")
//    }
//}