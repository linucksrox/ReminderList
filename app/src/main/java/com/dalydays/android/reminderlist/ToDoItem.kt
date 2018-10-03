package com.dalydays.android.reminderlist

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "item")
data class ToDoItem(@PrimaryKey(autoGenerate = true) var id: Long? = null,
                    @ColumnInfo(name = "description") var description: String? = null,
                    @ColumnInfo(name = "checked") var checked: Boolean = false
)