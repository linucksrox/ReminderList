package com.dalydays.android.reminderlist.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.FAIL
import androidx.room.Query

@Dao
interface ToDoItemDao {
    @Query("SELECT * FROM item")
    fun getAll(): List<ToDoItem>

    @Insert(onConflict = FAIL)
    fun insert(item: ToDoItem)

    @Query("DELETE FROM item")
    fun deleteAll()
}