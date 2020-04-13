package com.dalydays.android.reminderlist

import com.dalydays.android.reminderlist.util.Schedule
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.TimeUnit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ScheduleTest {
    @Test
    fun `building a schedule for 10 Seconds returns duration 10 and timeUnit SECONDS`() {
        val schedule = Schedule.build(10, "Seconds")
        assert(schedule.duration == 10L && schedule.timeUnit == TimeUnit.SECONDS)
    }

    @Test
    fun `building a schedule for 9 Minutes returns duration 9 and timeUnit MINUTES`() {
        val schedule = Schedule.build(9, "Minutes")
        assert(schedule.duration == 9L && schedule.timeUnit == TimeUnit.MINUTES)
    }

    @Test
    fun `building a schedule for 7 Hours returns duration 7 and timeUnit HOURS`() {
        val schedule = Schedule.build(7, "Hours")
        assert(schedule.duration == 7L && schedule.timeUnit == TimeUnit.HOURS)
    }

    @Test
    fun `building a schedule for 4 Days returns duration 4 and timeUnit DAYS`() {
        val schedule = Schedule.build(4, "Days")
        assert(schedule.duration == 4L && schedule.timeUnit == TimeUnit.DAYS)
    }

    @Test
    fun `building a schedule for 3 Weeks returns duration 21 and timeUnit DAYS`() {
        val schedule = Schedule.build(3, "Weeks")
        assert(schedule.duration == 21L && schedule.timeUnit == TimeUnit.DAYS)
    }

    @Test
    fun `building a schedule for 12 Months returns duration 365 and timeUnit DAYS`() {
        val schedule = Schedule.build(12, "Months")
        assert(schedule.duration == 365L && schedule.timeUnit == TimeUnit.DAYS)
    }

    @Test
    fun `building a schedule for 1 Years returns duration 365 and timeUnit DAYS`() {
        val schedule = Schedule.build(1, "Years")
        assert(schedule.duration == 365L && schedule.timeUnit == TimeUnit.DAYS)
    }

    @Test
    fun `getting toMillis for schedule of 2 Seconds should return 2000`() {
        val schedule = Schedule.build(2, "Seconds")
        assert(schedule.toMillis() == 2000L)
    }

    @Test
    fun `getting toMillis for schedule of 2 Minutes should return 120000`() {
        val schedule = Schedule.build(2, "Minutes")
        assert(schedule.toMillis() == 120000L)
    }

    @Test
    fun `getting toMillis for schedule of 2 Hours should return 7200000`() {
        val schedule = Schedule.build(2, "Hours")
        assert(schedule.toMillis() == 7200000L)
    }

    @Test
    fun `getting toMillis for schedule of 2 Days should return 172800000`() {
        val schedule = Schedule.build(2, "Days")
        assert(schedule.toMillis() == 172800000L)
    }

    @Test
    fun `getting toMillis for schedule of 2 Weeks should return 1209600000`() {
        val schedule = Schedule.build(2, "Weeks")
        assert(schedule.toMillis() == 1209600000L)
    }

    @Test
    fun `getting toMillis for schedule of 1 Months should return between 2419200000 and 2678400000`() {
        // between 28 and 31 days
        val scheduleInMillis = Schedule.build(1, "Months").toMillis()
        assert(scheduleInMillis in 2419200000L..2678400000L)
    }

    @Test
    fun `getting toMillis for schedule of 2 Months should return between 5097600000 and 5356800000`() {
        // between 28 and 31 days
        val scheduleInMillis = Schedule.build(2, "Months").toMillis()
        assert(scheduleInMillis in 5097600000L..5356800000L)
    }

    @Test
    fun `getting toMillis for schedule of 2 Years should return between 63072000000 and 63158400000`() {
        val scheduleInMillis = Schedule.build(2, "Years").toMillis()
        assert(scheduleInMillis in 63072000000L..63158400000L)
    }
}
