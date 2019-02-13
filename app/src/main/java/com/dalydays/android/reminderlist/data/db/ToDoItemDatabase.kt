package com.dalydays.android.reminderlist.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [ToDoItem::class], version = 1, exportSchema = true)
abstract class ToDoItemDatabase : RoomDatabase() {

    abstract fun ToDoItemDao(): ToDoItemDao

    companion object {
        @Volatile
        private var INSTANCE: ToDoItemDatabase? = null

        fun getDatabase(context: Context): ToDoItemDatabase {
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

    fun destroyInstance() {
        INSTANCE = null
    }
}