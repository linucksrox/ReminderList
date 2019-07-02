package com.dalydays.android.reminderlist.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.concurrent.TimeUnit

@Entity(tableName = "item")
data class ToDoItem(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "sort") var sort: Long? = null,
        @ColumnInfo(name = "description") var description: String,
        @ColumnInfo(name = "completed") var completed: Boolean = false,
        @ColumnInfo(name = "scheduled") var scheduled: Boolean = false,
        @ColumnInfo(name = "duration") var duration: Int? = null,
        @ColumnInfo(name = "time_unit") var timeUnit: TimeUnit? = null
)