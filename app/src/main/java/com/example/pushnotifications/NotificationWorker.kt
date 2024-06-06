package com.example.pushnotifications

import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.delay
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val notificationDao: NotificationDao by lazy {
        NotificationDatabase.getDatabase(context).notificationDao()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun doWork(): Result {
        val notifications = notificationDao.getNotificationsForWorker()

        for (notification in notifications) {
            showNotification(notification)
            delay(3000L)
            withContext(Dispatchers.Main) {
                NotificationManager.addNotification(notification)
            }
        }

        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showNotification(notification: NotificationEntity) {
        val context = applicationContext
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationLayout = RemoteViews(context.packageName, R.layout.notifcation).apply {
            setTextViewText(R.id.notification_title, notification.title)
            setTextViewText(R.id.notification_message, notification.message)
        }

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.rocco)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationLayout)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context, android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d(TAG, "Notification permission not granted")
                return
            }
            notify(notification.id, notificationBuilder.build())
        }
    }

    companion object {
        private const val CHANNEL_ID = "notification_channel"
    }
}
