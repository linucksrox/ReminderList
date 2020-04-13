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

            val realTimeUnit = gregorianCalendarToTimeUnit(calendarUnit)
            val realDuration = when (realTimeUnit) {
                TimeUnit.SECONDS -> TimeUnit.MILLISECONDS.toSeconds(timeDifferenceInMillis)
                TimeUnit.MINUTES -> TimeUnit.MILLISECONDS.toMinutes(timeDifferenceInMillis)
                TimeUnit.HOURS -> TimeUnit.MILLISECONDS.toHours(timeDifferenceInMillis)
                else -> TimeUnit.MILLISECONDS.toDays(timeDifferenceInMillis)
            }

            return Schedule(realDuration, realTimeUnit)
        }

        private fun gregorianCalendarToTimeUnit(gregorianCalendarUnit: Int): TimeUnit {
            // Scheduling is converted to DAYS for everything not smaller than a day
            return when (gregorianCalendarUnit) {
                GregorianCalendar.SECOND -> TimeUnit.SECONDS
                GregorianCalendar.MINUTE -> TimeUnit.MINUTES
                GregorianCalendar.HOUR -> TimeUnit.HOURS
                else -> TimeUnit.DAYS
            }
        }
    }

    fun toMillis(): Long {
        return when (timeUnit) {
            TimeUnit.SECONDS -> TimeUnit.SECONDS.toMillis(duration)
            TimeUnit.MINUTES -> TimeUnit.MINUTES.toMillis(duration)
            TimeUnit.HOURS -> TimeUnit.HOURS.toMillis(duration)
            else -> TimeUnit.DAYS.toMillis(duration)
        }
    }
}