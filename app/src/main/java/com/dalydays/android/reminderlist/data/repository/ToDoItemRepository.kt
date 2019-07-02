package com.dalydays.android.reminderlist.data.repository

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.db.ToDoItemDatabase

class ToDoItemRepository(context: Context) {

    private val toDoItemDao = ToDoItemDatabase.getInstance(context).toDoItemDao
    val allToDoItems: LiveData<List<ToDoItem>>

    init {
        allToDoItems = toDoItemDao.getAll()
    }

    @WorkerThread
    fun getItem(id: Long): ToDoItem {
        return toDoItemDao.getItem(id)
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