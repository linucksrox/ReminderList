package com.dalydays.android.reminderlist

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.dalydays.android.reminderlist.util.ReminderNotificationChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderListApp : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupNotificationChannels()
        }
    }

    private fun setupNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val generalName = getString(R.string.general_channel_name)
            val generalDescriptionText = getString(R.string.general_channel_description)
            val generalImportance = NotificationManager.IMPORTANCE_LOW
            val generalReminderChannel = NotificationChannel(ReminderNotificationChannel.GENERAL_CHANNEL, generalName, generalImportance).apply {
                description = generalDescriptionText
            }

            val singleName = getString(R.string.single_channel_name)
            val singleDescriptionText = getString(R.string.single_channel_description)
            val singleImportance = NotificationManager.IMPORTANCE_DEFAULT
            val singleReminderChannel = NotificationChannel(ReminderNotificationChannel.SINGLE_CHANNEL, singleName, singleImportance).apply {
                description = singleDescriptionText
            }

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(generalReminderChannel)
            notificationManager.createNotificationChannel(singleReminderChannel)
        }
    }
}