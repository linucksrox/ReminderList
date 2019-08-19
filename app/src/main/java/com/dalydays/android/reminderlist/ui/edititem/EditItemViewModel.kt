package com.dalydays.android.reminderlist.ui.edititem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import kotlinx.coroutines.*

class EditItemViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val newItemUiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository = ToDoItemRepository(application)

    private var _scheduled = MutableLiveData<Boolean>()
    val scheduled: LiveData<Boolean> = _scheduled

    private var _description = MutableLiveData<String?>()
    val description: LiveData<String?> = _description

    private var _duration = MutableLiveData<Long?>()
    val duration: LiveData<Long?> = _duration

    private var _timeUnit = MutableLiveData<String?>()
    val timeUnit: LiveData<String?> = _timeUnit

    init {
        _scheduled.value = false
    }

    fun addNewItem(description: String, recurring: Boolean, duration: Long, timeUnit: String) {
        insert(ToDoItem(description = description, recurring = recurring, duration = duration, timeUnit = timeUnit))
    }

    fun toggleSchedule() {
        // Reverse the value of scheduled
        _scheduled.value?.let {
            _scheduled.value = !it
        }
    }

    private fun insert(toDoItem: ToDoItem) = newItemUiScope.launch(Dispatchers.IO) {
        repository.insert(toDoItem)
    }

    fun initializeById(id: Long) {
        // If id is -1L then we're adding a new item, otherwise look up this item in the database and populate values
        if (id != -1L) {
            // Look up data
            newItemUiScope.launch(Dispatchers.IO) {
                val toDoItem = repository.getItem(id)
                withContext(Dispatchers.Main) {
                    _description.value = toDoItem.description
                    _scheduled.value = toDoItem.recurring
                    _duration.value = toDoItem.duration
                    _timeUnit.value = toDoItem.timeUnit
                }
            }
        }
    }
}