package com.dalydays.android.reminderlist.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.db.ToDoItemDatabase
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ChecklistViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: ToDoItemRepository
    val allToDoItems: LiveData<List<ToDoItem>>

    init {
        val toDoItemDao = ToDoItemDatabase.getInstance(application, scope).ToDoItemDao()
        repository = ToDoItemRepository(toDoItemDao)
        allToDoItems = repository.allToDoItems
    }

    fun insert(toDoItem: ToDoItem) = scope.launch(Dispatchers.IO) {
        repository.insert(toDoItem)
    }

    fun update(toDoItem: ToDoItem) = scope.launch(Dispatchers.IO) {
        repository.update(toDoItem)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}