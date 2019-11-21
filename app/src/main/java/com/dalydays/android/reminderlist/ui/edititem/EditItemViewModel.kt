package com.dalydays.android.reminderlist.ui.edititem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.WorkManager
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import com.dalydays.android.reminderlist.util.Event
import kotlinx.coroutines.*
import java.util.*

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

    private val _showDeleteMenuOption = MutableLiveData<Boolean>()
    val showDeleteMenuOption: LiveData<Boolean> = _showDeleteMenuOption

    private val _setToolbarTitleAddItem = MutableLiveData<Boolean>()
    val setToolbarTitleAddItem: LiveData<Boolean> = _setToolbarTitleAddItem

    private lateinit var toDoItem: ToDoItem

    init {
        _scheduled.value = false
        duration.value = 0L
        _saveButtonEnabled.value = false
        _showDeleteMenuOption.value = false
        initializeById(itemId)
    }

    private fun initializeById(id: Long) {
        // If id is -1L then we're adding a new item, otherwise look up this item in the database and populate values
        if (id != -1L) {
            // Look up data
            newItemUiScope.launch(Dispatchers.IO) {
                toDoItem = repository.getItem(id)
                withContext(Dispatchers.Main) {
                    description.value = toDoItem.description
                    _scheduled.value = toDoItem.recurring
                    duration.value = toDoItem.duration
                    _timeUnit.value = toDoItem.timeUnit
                }
            }
            _showDeleteMenuOption.value = true
        } else {
            _setToolbarTitleAddItem.value = true
        }
    }

    fun onFabButtonClicked() = newItemUiScope.launch {
        // navigate to new item screen
        _saveItemEvent.value = Event("clicked")
    }

    fun saveItem(itemId: Long, description: String, recurring: Boolean, duration: Long, timeUnit: String) {
        when (itemId) {
            -1L -> insert(ToDoItem(description = description, recurring = recurring, duration = duration, timeUnit = timeUnit))
            else -> {
                toDoItem.description = description
                toDoItem.recurring = recurring
                toDoItem.duration = duration
                toDoItem.timeUnit = timeUnit
                update(toDoItem)
            }
        }
    }

    fun deleteItem() {
        delete(toDoItem)
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
        // Cancel background job if there was one
        if (!toDoItem.backgroundWorkUUID.isNullOrEmpty()) {
            WorkManager.getInstance(getApplication()).cancelWorkById(UUID.fromString(toDoItem.backgroundWorkUUID))
        }
        repository.deleteItem(toDoItem)
    }
}