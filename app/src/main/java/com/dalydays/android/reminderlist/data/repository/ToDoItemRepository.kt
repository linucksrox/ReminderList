package com.dalydays.android.reminderlist.data.repository

import androidx.annotation.WorkerThread
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.db.ToDoItemDao

class ToDoItemRepositoryRepository(private val toDoItemDao: ToDoItemDao) {
    val allItems: List<ToDoItem> = toDoItemDao.getAll()

    @WorkerThread
    suspend fun insert(toDoItem: ToDoItem) {
        toDoItemDao.insert(toDoItem)
    }
}