package com.dalydays.android.reminderlist.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ToDoItemDao {
    @Query("SELECT * FROM item ORDER BY checked ASC, id ASC")
    fun getAll(): LiveData<List<ToDoItem>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(item: ToDoItem)

    @Query("DELETE FROM item")
    fun deleteAll()
}