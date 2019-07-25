package com.dalydays.android.reminderlist.util

import java.util.concurrent.TimeUnit

const val DAYS_PER_WEEK = 7

class Schedule(val duration: Long, val timeUnit: TimeUnit) {

    companion object {
        fun build(duration: Long, timeUnit: String): Schedule {
            val realDuration: Long
            val realTimeUnit: TimeUnit

            when (timeUnit) {
                "Seconds" -> {
                    realDuration = duration
                    realTimeUnit = TimeUnit.SECONDS
                }
                "Minutes" -> {
                    realDuration = duration
                    realTimeUnit = TimeUnit.MINUTES
                }
                "Hours" -> {
                    realDuration = duration
                    realTimeUnit = TimeUnit.HOURS
                }
                "Days" -> {
                    realDuration = duration
                    realTimeUnit = TimeUnit.DAYS
                }
                "Weeks" -> {
                    // convert duration value to days
                    realDuration = duration * DAYS_PER_WEEK
                    realTimeUnit = TimeUnit.DAYS
                }
                // TODO: handle months and years which adds complexity in calculating since they are not constant values
                else -> {
                    realDuration = 5L
                    realTimeUnit = TimeUnit.SECONDS
                }
            }

            return Schedule(realDuration, realTimeUnit)
        }
    }

    fun getTimeUnitAsString(): String {
        return when (timeUnit) {
            TimeUnit.SECONDS -> "Seconds"
            TimeUnit.MINUTES -> "Minutes"
            TimeUnit.HOURS -> "Hours"
            TimeUnit.DAYS -> "Days"
            else -> "Unknown"
        }
    }
}