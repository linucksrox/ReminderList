package com.dalydays.android.reminderlist.util

import java.util.*
import java.util.concurrent.TimeUnit

class Schedule(val duration: Long, val timeUnit: TimeUnit) {

    companion object {
        fun build(duration: Long, timeUnit: String): Schedule {
            val calendarUnit = when (timeUnit) {
                "Seconds" -> GregorianCalendar.SECOND
                "Minutes" -> GregorianCalendar.MINUTE
                "Hours" -> GregorianCalendar.HOUR
                "Days" -> GregorianCalendar.DAY_OF_MONTH
                "Weeks" -> GregorianCalendar.WEEK_OF_YEAR
                "Months" -> GregorianCalendar.MONTH
                "Years" -> GregorianCalendar.YEAR
                else -> GregorianCalendar.DAY_OF_MONTH
            }

            val calendar = GregorianCalendar.getInstance(Locale.getDefault())
            val startTimeMillis = calendar.timeInMillis
            calendar.add(calendarUnit, duration.toInt())
            val timeDifferenceInMillis = calendar.timeInMillis - startTimeMillis

            // Scheduling is converted to DAYS for everything not smaller than a day
            val realTimeUnit = when (calendarUnit) {
                GregorianCalendar.SECOND -> TimeUnit.SECONDS
                GregorianCalendar.MINUTE -> TimeUnit.MINUTES
                GregorianCalendar.HOUR -> TimeUnit.HOURS
                else -> TimeUnit.DAYS
            }
            val realDuration = when (realTimeUnit) {
                TimeUnit.SECONDS -> TimeUnit.MILLISECONDS.toSeconds(timeDifferenceInMillis)
                TimeUnit.MINUTES -> TimeUnit.MILLISECONDS.toMinutes(timeDifferenceInMillis)
                TimeUnit.HOURS -> TimeUnit.MILLISECONDS.toHours(timeDifferenceInMillis)
                else -> TimeUnit.MILLISECONDS.toDays(timeDifferenceInMillis)
            }

            return Schedule(realDuration, realTimeUnit)
        }
    }
}