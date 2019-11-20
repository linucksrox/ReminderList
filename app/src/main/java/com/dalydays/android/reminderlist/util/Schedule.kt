package com.dalydays.android.reminderlist.util

import java.util.*
import java.util.concurrent.TimeUnit

class Schedule(val duration: Long, val timeUnit: TimeUnit) {

    companion object {
        fun build(duration: Long, timeUnit: String): Schedule {
            val calendarUnit = when (timeUnit) {
                "Days" -> GregorianCalendar.DAY_OF_MONTH
                "Weeks" -> GregorianCalendar.WEEK_OF_YEAR
                "Months" -> GregorianCalendar.MONTH
                "Years" -> GregorianCalendar.YEAR
                else -> GregorianCalendar.DAY_OF_MONTH
            }

            val calendar = GregorianCalendar.getInstance(Locale.getDefault())
            val startTimeMillis = calendar.timeInMillis
            calendar.add(calendarUnit, duration.toInt())
            val timeDifferenceMillis = calendar.timeInMillis - startTimeMillis

            val realDuration = TimeUnit.MILLISECONDS.toDays(timeDifferenceMillis)
            val realTimeUnit = TimeUnit.DAYS

            return Schedule(realDuration, realTimeUnit)
        }
    }
}