package com.dalydays.android.reminderlist.ui.newitem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewItemViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val newItemUiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository = ToDoItemRepository(application)

    fun addNewItem(description: String, recurring: Boolean, duration: Long, timeUnit: String) {
        insert(ToDoItem(description = description, recurring = recurring, duration = duration, timeUnit = timeUnit))
    }

    private fun insert(toDoItem: ToDoItem) = newItemUiScope.launch(Dispatchers.IO) {
        repository.insert(toDoItem)
    }

}