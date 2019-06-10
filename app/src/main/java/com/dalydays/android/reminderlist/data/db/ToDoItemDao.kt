package com.dalydays.android.reminderlist.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoItemDao {
    @Query("SELECT * FROM item ORDER BY checked ASC, id ASC")
    fun getAll(): LiveData<List<ToDoItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ToDoItem): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: ToDoItem)

    @Query("DELETE FROM item")
    fun deleteAll()
}