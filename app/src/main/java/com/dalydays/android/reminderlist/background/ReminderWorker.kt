package com.dalydays.android.reminderlist.background

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import com.dalydays.android.reminderlist.ui.MainActivity
import kotlinx.coroutines.*

class ReminderWorker(private val context: Context, private val workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    private lateinit var reminderWorkerJob: Job

    override fun doWork(): Result {

        reminderWorkerJob = Job()
        val reminderWorkerScope = CoroutineScope(Dispatchers.IO + reminderWorkerJob)
        val toDoItemRepository = ToDoItemRepository(context)

        // Get the item id so we can uncheck it in the database
        val itemId = workerParameters.inputData.getLong("toDoItemId", 0L)

        // Update the item in the database (on a background thread)
        reminderWorkerScope.launch {
            val toDoItem = toDoItemRepository.getItem(itemId)
            // Proceed to notify if item is still checked, otherwise if it has been unchecked then we're already done
            if (toDoItem.completed) {
                toDoItem.completed = false
                toDoItem.backgroundWorkUUID = null
                toDoItemRepository.update(toDoItem)
                showNotification(context, context.getString(R.string.single_notification_title), toDoItem.description)
            }
        }

        return Result.success()
    }

    private fun showNotification(context: Context, title: String, text: String) {

        // Open the app when clicking the notification
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        // Build the notification
        val notificationId = 1

        val notificationBuilder = NotificationCompat.Builder(context, "reminder_channel_id")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        // Show the notification
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notificationBuilder.build())
        }
    }

    override fun onStopped() {
        super.onStopped()

        reminderWorkerJob.cancel()
    }
}