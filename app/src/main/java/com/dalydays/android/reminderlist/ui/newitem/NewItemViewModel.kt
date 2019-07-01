package com.dalydays.android.reminderlist.ui.newitem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class NewItemViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val newItemUiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val repository = ToDoItemRepository(application)

    fun addNewItem(description: String, scheduled: Boolean) {
        insert(ToDoItem(description = description, scheduled = scheduled))

        // Set up recurring work if item was scheduled
        if (scheduled) {
            // Build constraints
            val constraints = Constraints.Builder()
                    .build()

            // Schedule this worker to fire the notification based on the interval set for this item in the app
            val notificationWork = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setConstraints(constraints)
                    .setInitialDelay(2, TimeUnit.MINUTES) // TODO: set this based on the item's schedule
                    .build()

            // Queue up the work!
            WorkManager.getInstance().enqueue(notificationWork)
        }
    }

    private fun insert(toDoItem: ToDoItem) = newItemUiScope.launch(Dispatchers.IO) {
        repository.insert(toDoItem)
    }

}