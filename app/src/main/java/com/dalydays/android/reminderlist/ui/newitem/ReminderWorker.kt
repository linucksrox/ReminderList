package com.dalydays.android.reminderlist.ui.newitem

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dalydays.android.reminderlist.R

class ReminderWorker(private val context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {
    override fun doWork(): Result {

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