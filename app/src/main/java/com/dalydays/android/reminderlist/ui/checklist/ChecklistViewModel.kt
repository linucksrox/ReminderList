package com.dalydays.android.reminderlist.ui.checklist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import com.dalydays.android.reminderlist.background.ReminderWorker
import com.dalydays.android.reminderlist.util.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChecklistViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val checklistUiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository = ToDoItemRepository(application)
    val allToDoItems: LiveData<List<ToDoItem>>

    private var _navigateToNewToDoItem = MutableLiveData<Event<String>>()
    val navigateToNewToDoItem: LiveData<Event<String>>
        get() = _navigateToNewToDoItem

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
            // Build constraints
            val constraints = Constraints.Builder()
                    .build()

            // Build data that we'll send into the worker
            val itemId = requireNotNull(toDoItem.id)
            val data = Data.Builder()
                    .putLong("toDoItemId", itemId)
                    .build()

            // Schedule this worker to fire the notification based on the interval set for this item in the app
            val notificationWork = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setConstraints(constraints)
                    // override null safety here for now, should probably handle this differently to avoid breakage
                    .setInitialDelay(toDoItem.duration!!, toDoItem.timeUnit!!)
                    .setInputData(data)
                    .build()

            // Queue up the work!
            WorkManager.getInstance().enqueue(notificationWork)

            // Update the item's background work UUID in the database
            toDoItem.backgroundWorkUUID = notificationWork.id.toString()
            update(toDoItem)
        } else {
            // TODO: cancel the background task
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}