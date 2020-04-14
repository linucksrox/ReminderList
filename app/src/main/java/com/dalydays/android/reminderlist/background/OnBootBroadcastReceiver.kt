package com.dalydays.android.reminderlist.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import com.dalydays.android.reminderlist.util.NotificationMaker
import com.dalydays.android.reminderlist.util.ReminderNotificationType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "OnBootBroadcastReceiver"

class OnBootBroadcastReceiver : BroadcastReceiver() {

    private val applicationScope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            // If there are any uncompleted items on the list, fire general notification
            requireNotNull(context)
            val toDoItemRepository = ToDoItemRepository(context)

            applicationScope.launch {
                val uncheckedItemCount = toDoItemRepository.getUncheckedItemCount()
                if (uncheckedItemCount > 0) {
                    val notificationTitle = context.getString(R.string.general_notification_title)
                    val notificationText = context.resources.getQuantityString(R.plurals.numberOfUncheckedItems, uncheckedItemCount, uncheckedItemCount)
                    NotificationMaker.showNotification(context, notificationTitle, notificationText, ReminderNotificationType.GENERAL)
                }
            }
        }
    }
}