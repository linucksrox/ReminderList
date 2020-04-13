package com.dalydays.android.reminderlist.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.util.NotificationMaker
import com.dalydays.android.reminderlist.util.ReminderNotificationType

private const val TAG = "OnBootBroadcastReceiver"

class OnBootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            // If there are any uncompleted items on the list, fire general notification
            // TODO: check for uncompleted items before firing notification here
            // TODO: this code might need to be refactored so that the logic is somewhere else
            requireNotNull(context)
            val notificationTitle = context.getString(R.string.general_notification_title)
            val notificationText = context.getString(R.string.general_notification_text)
            NotificationMaker.showNotification(context, notificationTitle, notificationText, ReminderNotificationType.GENERAL)
        }
    }
}