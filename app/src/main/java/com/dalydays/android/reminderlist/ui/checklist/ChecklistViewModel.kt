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
        toDoItem.completed = !toDoItem.completed
        update(toDoItem)

        // If the user just checked off an item and it has scheduling enabled, then schedule the reminder
        scheduleRedoItem(toDoItem)

        // If the user unchecks an item, cancel the schedule
        // TODO: unschedule background task
    }

    private fun scheduleRedoItem(toDoItem: ToDoItem) {
        if (toDoItem.completed && toDoItem.scheduled) {
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
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}