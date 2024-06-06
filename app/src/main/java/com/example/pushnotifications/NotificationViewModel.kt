package com.example.pushnotifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotificationRepository
    val allNotifications: LiveData<List<NotificationEntity>>

    init {
        val notificationDao = NotificationDatabase.getDatabase(application).notificationDao()
        repository = NotificationRepository(notificationDao)
        allNotifications = repository.allNotifications
    }

    fun insert(notification: NotificationEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(notification)
    }
}
