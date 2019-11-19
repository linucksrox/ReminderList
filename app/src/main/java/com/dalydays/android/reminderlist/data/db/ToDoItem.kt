package com.dalydays.android.reminderlist.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class ToDoItem(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "sort") var sort: Long? = null,
        @ColumnInfo(name = "description") var description: String,
        @ColumnInfo(name = "completed") var completed: Boolean = false,
        @ColumnInfo(name = "recurring") var recurring: Boolean = false,
        @ColumnInfo(name = "duration") var duration: Long? = null,
        @ColumnInfo(name = "time_unit") var timeUnit: String? = null,
        @ColumnInfo(name = "background_work_uuid") var backgroundWorkUUID: String? = null
) {
    fun getDurationString() = duration.toString() + " " + timeUnit
}