package com.dalydays.android.reminderlist.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.ui.MainActivity

class NotificationMaker {

    companion object {
        const val GENERAL_CHANNEL = "general_channel_id"
        private const val GENERAL_TYPE = 1

        fun showGeneralNotification(context: Context, count: Int) {
            // No notification necessary if there are no items on the list
            if (count > 0) {
                val title = context.getString(R.string.general_notification_title)
                val text = context.resources.getQuantityString(R.plurals.numberOfUncheckedItems, count, count)

                // Open the app when clicking the notification
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent = PendingIntent.getActivity(context, GENERAL_TYPE, intent, 0)

                val notification = NotificationCompat.Builder(context, GENERAL_CHANNEL)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setOnlyAlertOnce(true)
                        .build()

                // Show the notification
                with(NotificationManagerCompat.from(context)) {
                    notify(GENERAL_TYPE, notification)
                }
            }
        }
    }
}