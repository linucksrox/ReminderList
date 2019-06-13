package com.dalydays.android.reminderlist.ui.checklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChecklistViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val checklistUiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository = ToDoItemRepository(application)
    val allToDoItems: LiveData<List<ToDoItem>>

    private var _navigateToNewToDoItem = MutableLiveData<Boolean?>()
    val navigateToNewToDoItem: LiveData<Boolean?>
        get() = _navigateToNewToDoItem

    fun doneNavigating() {
        // reset all navigation events here
        _navigateToNewToDoItem.value = null
    }

    init {
        allToDoItems = repository.allToDoItems
    }

    private fun insert(toDoItem: ToDoItem) = checklistUiScope.launch(Dispatchers.IO) {
        repository.insert(toDoItem)
    }

    private fun update(toDoItem: ToDoItem) = checklistUiScope.launch(Dispatchers.IO) {
        repository.update(toDoItem)
    }

    fun onFabButtonClicked() = checklistUiScope.launch {
        // navigate to new item screen
        _navigateToNewToDoItem.value = true
    }

    fun toggleCheckbox(toDoItem: ToDoItem) {
        toDoItem.checked = !toDoItem.checked
        update(toDoItem)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}