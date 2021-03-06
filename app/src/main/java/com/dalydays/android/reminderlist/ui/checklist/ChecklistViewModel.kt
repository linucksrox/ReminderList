package com.dalydays.android.reminderlist.ui.checklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import com.dalydays.android.reminderlist.background.ReminderWorker
import com.dalydays.android.reminderlist.util.Event
import com.dalydays.android.reminderlist.util.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

const val TAG_NAME = "scheduledfor"

class ChecklistViewModel(application: Application, deletedDescription: String) : AndroidViewModel(application) {

    private val repository = ToDoItemRepository(application)
    val allToDoItems: LiveData<List<ToDoItem>>

    private var _navigateToNewToDoItem = MutableLiveData<Event<String>>()
    val navigateToNewToDoItem: LiveData<Event<String>>
        get() = _navigateToNewToDoItem

    private var _showDeletedSnackBar = MutableLiveData<Event<String>>()
    val showDeletedSnackBar: LiveData<Event<String>>
        get() = _showDeletedSnackBar

    private var _showScheduledSnackBar = MutableLiveData<Event<String>>()
    val showScheduledSnackBar: LiveData<Event<String>>
        get() = _showScheduledSnackBar

    init {
        allToDoItems = repository.allToDoItems
        if (deletedDescription != "default-nothing-deleted") {
            _showDeletedSnackBar.value = Event(deletedDescription)
        }
    }

    private fun update(toDoItem: ToDoItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(toDoItem)
    }

    fun onFabButtonClicked() = viewModelScope.launch {
        // navigate to new item screen
        _navigateToNewToDoItem.value = Event("clicked")
    }

    fun toggleCheckbox(toDoItem: ToDoItem) {
        toDoItem.completed = !toDoItem.completed
        update(toDoItem)

        // If the user just checked off an item and it has scheduling enabled, then schedule the reminder
        if (toDoItem.recurring) {
            scheduleOrCancelRedoBackgroundTask(toDoItem)
        }
    }

    private fun scheduleOrCancelRedoBackgroundTask(toDoItem: ToDoItem) {
        if (toDoItem.completed) {
            // Build data that we'll send into the worker
            val itemId = requireNotNull(toDoItem.id)
            val data = Data.Builder()
                    .putLong("toDoItemId", itemId)
                    .build()

            // Convert the duration and timeUnit to something that the work request builder understands
            val schedule = Schedule.build(toDoItem.duration!!, toDoItem.timeUnit!!)

            // Get scheduled time/date in milliseconds so that we can add it to a WorkInfo tag
            // in order to see when the reminder will happen for checked items
            val nowInMillis = GregorianCalendar.getInstance(Locale.getDefault()).timeInMillis
            val scheduleMillis = nowInMillis + schedule.toMillis()

            val notificationWork = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInitialDelay(schedule.duration, schedule.timeUnit)
                    .setInputData(data)
                    .addTag("$TAG_NAME:$scheduleMillis")
                    .build()

            // Queue up the work!
            WorkManager.getInstance(getApplication()).enqueue(notificationWork)

            // Update the item's background work UUID in the database
            toDoItem.backgroundWorkUUID = notificationWork.id.toString()
            update(toDoItem)

            // Notify that the item was scheduled
            _showScheduledSnackBar.value = Event("${toDoItem.duration} ${toDoItem.timeUnit}")
        } else {
            if (!toDoItem.backgroundWorkUUID.isNullOrEmpty()) {
                WorkManager.getInstance(getApplication()).cancelWorkById(UUID.fromString(toDoItem.backgroundWorkUUID))
                toDoItem.backgroundWorkUUID = null
                update(toDoItem)
            }
        }
    }
}