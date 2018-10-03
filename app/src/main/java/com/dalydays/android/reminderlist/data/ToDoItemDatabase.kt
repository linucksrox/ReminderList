package com.dalydays.android.reminderlist.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(ToDoItem::class), version = 1)
abstract class ToDoItemDatabase : RoomDatabase() {

    abstract fun ToDoItemDao(): ToDoItemDao

    companion object {
        private var INSTANCE: ToDoItemDatabase? = null

        fun getInstance(context: Context): ToDoItemDatabase? {
            if (INSTANCE == null) {
                synchronized(ToDoItemDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            ToDoItemDatabase::class.java, "todoitem.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}