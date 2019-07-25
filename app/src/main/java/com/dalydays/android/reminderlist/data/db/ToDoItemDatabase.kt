package com.dalydays.android.reminderlist.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ToDoItem::class], version = 1, exportSchema = true)
abstract class ToDoItemDatabase : RoomDatabase() {

    abstract val toDoItemDao: ToDoItemDao

    companion object {
        @Volatile
        private var INSTANCE: ToDoItemDatabase? = null

        fun getInstance(context: Context): ToDoItemDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ToDoItemDatabase::class.java,
                        "todoitem"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}