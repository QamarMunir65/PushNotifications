package com.example.pushnotifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var notificationViewModel: NotificationViewModel

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        createNotificationChannel()

        notificationViewModel = ViewModelProvider.AndroidViewModelFactory
            .getInstance(application)
            .create(NotificationViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Message received from: ${remoteMessage.from}")

        val title = remoteMessage.notification?.title ?: "Default Title"
        val message = remoteMessage.notification?.body ?: "Default Message"
        Log.d(TAG, "Notification title: $title, message: $message")

        // Save notification to the database
        saveNotificationToDatabase(title, message)

        generateNotification(title, message)
    }

    private fun saveNotificationToDatabase(title: String, message: String) {
        val notificationEntity = NotificationEntity(title = title, message = message)
        notificationViewModel.insert(notificationEntity)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun generateNotification(title: String, message: String) {
        Log.d(TAG, "Generating notification")

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationLayout = RemoteViews(packageName, R.layout.notifcation).apply {
            setTextViewText(R.id.notification_title, title)
            setTextViewText(R.id.notification_message, message)
        }

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.rocco)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MyFirebaseMessagingService,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "Notification permission not granted")
                return
            }
            Log.d(TAG, "Displaying notification")
            notify(NOTIFICATION_ID, notificationBuilder.build())
        }
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
            Log.d(TAG, "Notification channel created")
        }
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        private const val CHANNEL_ID = "notification_channel"
        private const val NOTIFICATION_ID = 1
    }
}
