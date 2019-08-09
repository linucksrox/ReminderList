package com.dalydays.android.reminderlist.ui.edititem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import com.dalydays.android.reminderlist.util.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditItemViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val newItemUiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository = ToDoItemRepository(application)

    private var _toggleScheduleEvent = MutableLiveData<Event<String>>()
    val toggleScheduleEvent: LiveData<Event<String>>
        get() = _toggleScheduleEvent

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
}