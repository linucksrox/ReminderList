package com.dalydays.android.reminderlist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class ToDoItem(@PrimaryKey(autoGenerate = true) var id: Long? = null,
                    @ColumnInfo(name = "description") var description: String? = null,
                    @ColumnInfo(name = "checked") var checked: Boolean = false
)