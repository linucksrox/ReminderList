package com.dalydays.android.reminderlist

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class ReminderListApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // set up notification channel required for Android Oreo and later
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = "reminder_channel_id"
            val name = getString(R.string.reminder_channel_name)
            val descriptionText = getString(R.string.reminder_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val reminderChannel = NotificationChannel(CHANNEL_ID, name, importance)
            reminderChannel.description = descriptionText

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(reminderChannel)
        }
    }
}