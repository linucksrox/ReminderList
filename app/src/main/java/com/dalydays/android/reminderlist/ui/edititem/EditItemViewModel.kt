package com.dalydays.android.reminderlist.ui.edititem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import com.dalydays.android.reminderlist.util.Event
import kotlinx.coroutines.*

class EditItemViewModel(application: Application, itemId: Long) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val newItemUiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository = ToDoItemRepository(application)

    private var _scheduled = MutableLiveData<Boolean>()
    val scheduled: LiveData<Boolean> = _scheduled
    val inputEnabled: LiveData<Boolean> = _scheduled

    val description = MutableLiveData<String?>()

    val duration = MutableLiveData<Long?>()

    private var _timeUnit = MutableLiveData<String?>()
    val timeUnit: LiveData<String?> = _timeUnit

    private var _saveItemEvent = MutableLiveData<Event<String>>()
    val saveItemEvent: LiveData<Event<String>> = _saveItemEvent

    private var _saveButtonEnabled = MutableLiveData<Boolean>()
    val saveButtonEnabled: LiveData<Boolean> = _saveButtonEnabled

    private val _descriptionError = MutableLiveData<Boolean>()
    val descriptionError: LiveData<Boolean> = _descriptionError

    init {
        _scheduled.value = false
        duration.value = 0L
        _saveButtonEnabled.value = false
        initializeById(itemId)
    }

    private fun initializeById(id: Long) {
        // If id is -1L then we're adding a new item, otherwise look up this item in the database and populate values
        if (id != -1L) {
            // Look up data
            newItemUiScope.launch(Dispatchers.IO) {
                val toDoItem = repository.getItem(id)
                withContext(Dispatchers.Main) {
                    description.value = toDoItem.description
                    _scheduled.value = toDoItem.recurring
                    duration.value = toDoItem.duration
                    _timeUnit.value = toDoItem.timeUnit
                }
            }
        }
    }

    fun onFabButtonClicked() = newItemUiScope.launch {
        // navigate to new item screen
        _saveItemEvent.value = Event("clicked")
    }

    fun saveItem(itemId: Long, description: String, recurring: Boolean, duration: Long, timeUnit: String) {
        when (itemId) {
            -1L -> insert(ToDoItem(description = description, recurring = recurring, duration = duration, timeUnit = timeUnit))
            else -> update(ToDoItem(id = itemId, description = description, recurring = recurring, duration = duration, timeUnit = timeUnit))
        }
    }

    fun deleteItem(itemId: Long, description: String, recurring: Boolean, duration: Long, timeUnit: String) {
        delete(ToDoItem(id = itemId, description = description, recurring = recurring, duration = duration, timeUnit = timeUnit))
    }

    fun toggleSchedule() {
        // Reverse the value of scheduled
        _scheduled.value?.let {
            _scheduled.value = !it
        }
    }

    fun validateInput() {
        // require not blank description
        val descriptionIsBlank = description.value.isNullOrBlank()
        _descriptionError.value = descriptionIsBlank

        _saveButtonEnabled.value = !(descriptionIsBlank)
    }

    private fun insert(toDoItem: ToDoItem) = newItemUiScope.launch(Dispatchers.IO) {
        repository.insert(toDoItem)
    }

    private fun update(toDoItem: ToDoItem) = newItemUiScope.launch(Dispatchers.IO) {
        repository.update(toDoItem)
    }

    private fun delete(toDoItem: ToDoItem) = newItemUiScope.launch(Dispatchers.IO) {
        repository.deleteItem(toDoItem)
    }
}