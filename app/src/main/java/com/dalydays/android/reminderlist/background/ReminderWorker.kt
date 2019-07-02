package com.dalydays.android.reminderlist.background

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.data.db.ToDoItemDatabase
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import kotlinx.coroutines.*

class ReminderWorker(private val context: Context, private val workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    override fun doWork(): Result {

        val reminderWorkerJob = Job()
        val reminderWorkerScope = CoroutineScope(Dispatchers.IO + reminderWorkerJob)
        val toDoItemDao = ToDoItemDatabase.getInstance(context).toDoItemDao

        // Get the item id so we can uncheck it in the database
        val itemId = workerParameters.inputData.getLong("toDoItemId", 0L)

        // Update the item in the database (on a background thread)
        reminderWorkerScope.launch {
            val toDoItem = toDoItemDao.getItem(itemId)
            toDoItem.completed = !toDoItem.completed
            toDoItemDao.update(toDoItem)
        }

        // build the notification
        val notificationId = 1

        val notificationBuilder = NotificationCompat.Builder(context, "reminder_channel_id")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("itworks!")
                .setContentText("this is a notification from ReminderWorker")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // show the notification
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notificationBuilder.build())
        }

        return Result.success()
    }
}