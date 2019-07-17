package com.dalydays.android.reminderlist.util

import androidx.room.TypeConverter
import java.util.*
import java.util.concurrent.TimeUnit

class CustomTypeConverters {
    @TypeConverter
    fun stringToTimeUnit(timeUnit: String): TimeUnit? {
        return when (timeUnit) {
            "seconds" -> TimeUnit.SECONDS
            "minutes" -> TimeUnit.MINUTES
            "hours" -> TimeUnit.HOURS
            "days" -> TimeUnit.DAYS
            else -> null
        }
    }

    @TypeConverter
    fun timeUnitToString(timeUnit: TimeUnit): String? {
        return when (timeUnit) {
            TimeUnit.SECONDS -> "seconds"
            TimeUnit.MINUTES -> "minutes"
            TimeUnit.HOURS -> "hours"
            TimeUnit.DAYS -> "days"
            else -> null
        }
    }
}