package com.dalydays.android.reminderlist.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import com.dalydays.android.reminderlist.util.NotificationMaker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnBootBroadcastReceiver : BroadcastReceiver() {

    private val applicationScope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            // If there are any uncompleted items on the list, fire general notification
            requireNotNull(context)
            val toDoItemRepository = ToDoItemRepository(context)

            applicationScope.launch {
                NotificationMaker.showGeneralNotification(context,
                        toDoItemRepository.getUncheckedItemCount())
            }
        }
    }
}