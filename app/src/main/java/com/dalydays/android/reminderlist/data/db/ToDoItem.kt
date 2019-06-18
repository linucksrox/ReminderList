package com.dalydays.android.reminderlist.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class ToDoItem(
        @PrimaryKey(autoGenerate = true) var id: Long? = null,
        @ColumnInfo(name = "sort") var sort: Long? = null,
        @ColumnInfo(name = "description") var description: String,
        @ColumnInfo(name = "checked") var checked: Boolean = false
)