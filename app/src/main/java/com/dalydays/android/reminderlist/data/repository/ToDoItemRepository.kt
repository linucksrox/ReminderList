package com.dalydays.android.reminderlist.data.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.db.ToDoItemDao

class ToDoItemRepository(private val toDoItemDao: ToDoItemDao) {

    val allToDoItems: LiveData<List<ToDoItem>> = toDoItemDao.getAll()

    @WorkerThread
    suspend fun insert(toDoItem: ToDoItem) {
        toDoItemDao.insert(toDoItem)
    }

    @WorkerThread
    suspend fun update(toDoItem: ToDoItem) {
        toDoItemDao.update(toDoItem)
    }
}