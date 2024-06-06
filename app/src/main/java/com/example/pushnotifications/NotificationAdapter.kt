package com.example.pushnotifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var notifications: MutableList<NotificationEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    fun setNotifications(newNotifications: List<NotificationEntity>) {
        this.notifications = newNotifications.toMutableList()
        notifyDataSetChanged()
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.notification_title)
        private val messageTextView: TextView = itemView.findViewById(R.id.notification_message)

        fun bind(notification: NotificationEntity) {
            titleTextView.text = notification.title
            messageTextView.text = notification.message
        }
    }
}
