package com.example.pushnotifications

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: NotificationEntity): Long

    @Query("SELECT * FROM notifications")
    fun getAllNotifications(): LiveData<List<NotificationEntity>>

    @Query("SELECT * FROM notifications")
    suspend fun getNotificationsForWorker(): List<NotificationEntity>
}

