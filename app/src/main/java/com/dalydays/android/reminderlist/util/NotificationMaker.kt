package com.dalydays.android.reminderlist.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.ui.MainActivity

const val ONE_YEAR_IN_MILLISECONDS = 31449600000L

class NotificationMaker {

    companion object {
        const val GENERAL_CHANNEL = "general_channel_id"
        const val SINGLE_CHANNEL = "single_channel_id"
        private const val GENERAL_TYPE = 1
        private const val SINGLE_TYPE = 2

        fun showGeneralNotification(context: Context, count: Int) {
            // No notification necessary if there are no items on the list
            if (count > 0) {
                val title = context.getString(R.string.general_notification_title)
                val text = context.resources.getQuantityString(R.plurals.numberOfUncheckedItems, count, count)
                showNotification(context, title, text, GENERAL_TYPE)
            }
        }

        fun showSingleNotification(context: Context, text: String) {
            val title = context.getString(R.string.single_notification_title)
            showNotification(context, title, text, SINGLE_TYPE)
        }

        private fun showNotification(context: Context, title: String, text: String, type: Int) {
            // Open the app when clicking the notification
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(context, type, intent, 0)

            val channel = when(type) {
                GENERAL_TYPE -> GENERAL_CHANNEL
                SINGLE_TYPE -> SINGLE_CHANNEL
                else -> GENERAL_CHANNEL
            }

            val notification = NotificationCompat.Builder(context, channel)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setTimeoutAfter(ONE_YEAR_IN_MILLISECONDS) // notifications were timing out on Android 11 - Long.MAX_VALUE wasn't working, so let's try setting the timeout to 1 year

            if (type == GENERAL_TYPE) {
                notification.setOnlyAlertOnce(true)
            }

            if (type == SINGLE_TYPE) {
                notification.setAutoCancel(true)
            }

            // Show the notification
            with(NotificationManagerCompat.from(context)) {
                notify(type, notification.build())
            }
        }
    }
}