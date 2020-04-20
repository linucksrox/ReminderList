package com.dalydays.android.reminderlist.background

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dalydays.android.reminderlist.data.repository.ToDoItemRepository
import com.dalydays.android.reminderlist.util.NotificationMaker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ReminderWorker(private val context: Context, private val workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    private lateinit var reminderWorkerJob: Job

    override fun doWork(): Result {

        reminderWorkerJob = Job()
        val reminderWorkerScope = CoroutineScope(Dispatchers.IO + reminderWorkerJob)
        val toDoItemRepository = ToDoItemRepository(context)

        // Get the item id so we can uncheck it in the database
        val itemId = workerParameters.inputData.getLong("toDoItemId", 0L)

        // Update the item in the database (on a background thread)
        reminderWorkerScope.launch {
            // Proceed to notify if item is still checked, otherwise if it has been unchecked then we're already done
            val toDoItem = toDoItemRepository.getItem(itemId)
            if (toDoItem.completed) {
                toDoItem.completed = false
                toDoItem.backgroundWorkUUID = null
                toDoItemRepository.update(toDoItem)
                NotificationMaker.showGeneralNotification(context, toDoItemRepository.getUncheckedItemCount())
            }
        }

        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()

        reminderWorkerJob.cancel()
    }
}