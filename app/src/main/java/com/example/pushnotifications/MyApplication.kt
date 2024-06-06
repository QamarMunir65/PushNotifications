package com.example.pushnotifications

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        setupPeriodicWork()
    }

    private fun setupPeriodicWork() {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(3, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "NotificationWork",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel Name"
            val descriptionText = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "notification_channel"
    }
}

