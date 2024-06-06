package com.example.pushnotifications

import androidx.lifecycle.LiveData

class NotificationRepository(private val notificationDao: NotificationDao) {

    val allNotifications: LiveData<List<NotificationEntity>> = notificationDao.getAllNotifications()

    suspend fun insert(notification: NotificationEntity) {
        notificationDao.insert(notification)
    }
}
