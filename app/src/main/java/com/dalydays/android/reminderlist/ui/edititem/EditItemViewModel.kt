package com.dalydays.android.reminderlist.ui.edititem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import com.dalydays.android.reminderlist.util.Event
import kotlinx.coroutines.*

class EditItemViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val newItemUiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository = ToDoItemRepository(application)

    private var _toggleScheduleEvent = MutableLiveData<Event<String>>()
    val toggleScheduleEvent: LiveData<Event<String>>
        get() = _toggleScheduleEvent

    private var _description = MutableLiveData<String?>()
    val description: LiveData<String?>
        get() = _description

    private var _recurring = MutableLiveData<Boolean?>()
    val recurring: LiveData<Boolean?>
        get() = _recurring

    fun addNewItem(description: String, recurring: Boolean, duration: Long, timeUnit: String) {
        insert(ToDoItem(description = description, recurring = recurring, duration = duration, timeUnit = timeUnit))
    }

    private fun insert(toDoItem: ToDoItem) = newItemUiScope.launch(Dispatchers.IO) {
        repository.insert(toDoItem)
    }

    fun toggleSchedule() {
        // Notify fragment to enable/disable input views based on the state of the schedule toggle
        _toggleScheduleEvent.value = Event("switched")
    }

    fun initializeById(id: Long) {
        // If id is -1L then we're adding a new item, otherwise look up this item in the database and populate values
        if (id != -1L) {
            // look up data
            newItemUiScope.launch(Dispatchers.IO) {
                val toDoItem = repository.getItem(id)
                withContext(Dispatchers.Main) {
                    // TODO: use livedata so we can observe from fragment and update UI when data is updated from repo
                    _description.value = toDoItem.description
//                    duration = toDoItem.duration
//                    timeUnit = toDoItem.timeUnit
                    _recurring.value = toDoItem.recurring
                }
            }
        }
    }
}