package com.dalydays.android.reminderlist.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.ui.MainActivity

class ReminderNotificationChannel() {
    companion object {
        const val GENERAL_CHANNEL = "general_channel_id"
        const val SINGLE_CHANNEL = "single_channel_id"
    }
}

class ReminderNotificationType() {
    companion object {
        const val SINGLE = 1
        const val GENERAL = 2
    }
}

class NotificationMaker {
    companion object {
        fun showGeneralNotification(context: Context, count: Int) {
            // No notification necessary if there are no items on the list
            if (count > 0) {
                val title = context.getString(R.string.general_notification_title)
                val text = context.resources.getQuantityString(R.plurals.numberOfUncheckedItems, count, count)
                showNotification(context, title, text, ReminderNotificationType.GENERAL)
            }
        }

        fun showSingleNotification(context: Context, text: String) {
            val title = context.getString(R.string.single_notification_title)
            showNotification(context, title, text, ReminderNotificationType.SINGLE)
        }

        private fun showNotification(context: Context, title: String, text: String, notificationType: Int) {
            // Open the app when clicking the notification
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(context, notificationType, intent, 0)

            val reminderChannel = when (notificationType) {
                ReminderNotificationType.SINGLE -> ReminderNotificationChannel.SINGLE_CHANNEL
                ReminderNotificationType.GENERAL -> ReminderNotificationChannel.GENERAL_CHANNEL
                else -> ReminderNotificationChannel.GENERAL_CHANNEL
            }

            val notificationBuilder = NotificationCompat.Builder(context, reminderChannel)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

            // Show the notification
            with(NotificationManagerCompat.from(context)) {
                notify(notificationType, notificationBuilder.build())
            }
        }
    }
}