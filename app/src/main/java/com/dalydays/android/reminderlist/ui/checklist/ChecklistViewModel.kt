package com.dalydays.android.reminderlist.ui.checklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.db.ToDoItemDao
import com.dalydays.android.reminderlist.data.db.ToDoItemDatabase
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.random.Random

class ChecklistViewModel(
        database: ToDoItemDao,
        application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val checklistUiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository: ToDoItemRepository
    val allToDoItems = database.getAll()

    private var _addedActivityEvent = MutableLiveData<String>()
    val addedActivityEvent: LiveData<String>
        get() = _addedActivityEvent

    fun addedActivityEventComplete() {
        _addedActivityEvent.value = null
    }

    init {
        val toDoItemDao = ToDoItemDatabase.getInstance(application).toDoItemDao
        repository = ToDoItemRepository(toDoItemDao)
    }

    fun onCheckboxClicked(toDoItem: ToDoItem) {
        // Toggle the checked state and update this item in the repository
        toDoItem.checked = !toDoItem.checked
        update(toDoItem)
    }

    private fun insert(toDoItem: ToDoItem) = checklistUiScope.launch(Dispatchers.IO) {
        repository.insert(toDoItem)
    }

    private fun update(toDoItem: ToDoItem) = checklistUiScope.launch(Dispatchers.IO) {
        repository.update(toDoItem)
    }

    fun onFabButtonClicked() = checklistUiScope.launch(Dispatchers.IO) {
        val description = "Item ${Random.nextInt(1, 999)}"
        val checked = Random.nextBoolean()
        _addedActivityEvent.value = "Added new item: $description"
        insert(ToDoItem(description = description, checked = checked))
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}