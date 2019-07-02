package com.dalydays.android.reminderlist.util

import androidx.room.TypeConverter
import java.util.concurrent.TimeUnit

class TimeUnitTypeConverter {
    @TypeConverter
    fun toTimeUnit(timeUnit: String): TimeUnit? {
        return when (timeUnit) {
            "seconds" -> TimeUnit.SECONDS
            "minutes" -> TimeUnit.MINUTES
            "hours" -> TimeUnit.HOURS
            "days" -> TimeUnit.DAYS
            else -> null
        }
    }

    @TypeConverter
    fun toString(timeUnit: TimeUnit): String? {
        return when (timeUnit) {
            TimeUnit.SECONDS -> "seconds"
            TimeUnit.MINUTES -> "minutes"
            TimeUnit.HOURS -> "hours"
            TimeUnit.DAYS -> "days"
            else -> null
        }
    }
}