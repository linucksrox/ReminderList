package com.dalydays.android.reminderlist.data.repository

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.db.ToDoItemDatabase

class ToDoItemRepository(application: Application) {

    private val toDoItemDao = ToDoItemDatabase.getInstance(application).toDoItemDao
    val allToDoItems: LiveData<List<ToDoItem>>

    init {
        allToDoItems = toDoItemDao.getAll()
    }

    @WorkerThread
    suspend fun insert(toDoItem: ToDoItem) {
        toDoItemDao.insert(toDoItem)
    }

    @WorkerThread
    suspend fun update(toDoItem: ToDoItem) {
        toDoItemDao.update(toDoItem)
    }
}