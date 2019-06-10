package com.dalydays.android.reminderlist.data.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.db.ToDoItemDao

class ToDoItemRepository(private val toDoItemDao: ToDoItemDao) {

    @WorkerThread
    fun insert(toDoItem: ToDoItem) {
        toDoItemDao.insert(toDoItem)
    }

    @WorkerThread
    fun update(toDoItem: ToDoItem) {
        toDoItemDao.update(toDoItem)
    }
}