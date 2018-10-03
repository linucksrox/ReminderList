package com.dalydays.android.reminderlist.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.FAIL
import android.arch.persistence.room.Query

@Dao
interface ToDoItemDao {
    @Query("SELECT * FROM item")
    fun getAll(): List<ToDoItem>

    @Insert(onConflict = FAIL)
    fun insert(item: ToDoItem)

    @Query("DELETE FROM item")
    fun deleteAll()
}