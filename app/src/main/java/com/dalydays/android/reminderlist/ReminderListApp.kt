package com.dalydays.android.reminderlist

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderListApp : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    companion object {
        const val GENERAL_CHANNEL_ID = "general_channel_id"
        const val SINGLE_CHANNEL_ID = "single_channel_id"
    }

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
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Build General Channel
            val generalName = getString(R.string.general_channel_name)
            val generalDescriptionText = getString(R.string.general_channel_description)
            val generalImportance = NotificationManager.IMPORTANCE_LOW
            val generalReminderChannel = NotificationChannel(GENERAL_CHANNEL_ID, generalName, generalImportance).apply {
                description = generalDescriptionText
            }
            notificationManager.createNotificationChannel(generalReminderChannel)

            // Build Single Channel
            val singleName = getString(R.string.single_channel_name)
            val singleDescriptionText = getString(R.string.single_channel_description)
            val singleImportance = NotificationManager.IMPORTANCE_DEFAULT
            val singleReminderChannel = NotificationChannel(SINGLE_CHANNEL_ID, singleName, singleImportance).apply {
                description = singleDescriptionText
            }
            notificationManager.createNotificationChannel(singleReminderChannel)
        }
    }
}