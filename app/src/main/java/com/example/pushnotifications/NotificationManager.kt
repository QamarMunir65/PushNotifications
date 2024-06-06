package com.example.pushnotifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object NotificationManager {
    private val _notifications = MutableLiveData<MutableList<NotificationEntity>>(mutableListOf())
    val notifications: LiveData<MutableList<NotificationEntity>> get() = _notifications

    fun addNotification(notification: NotificationEntity) {
        val currentList = _notifications.value ?: mutableListOf()
        currentList.add(notification)
        _notifications.value = currentList
    }
}
