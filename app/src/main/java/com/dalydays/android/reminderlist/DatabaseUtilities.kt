package com.dalydays.android.reminderlist

import com.dalydays.android.reminderlist.data.ToDoItem
import com.dalydays.android.reminderlist.data.ToDoItemDatabase

class DatabaseUtilities {
    companion object {
        fun insertToDoItem(item: ToDoItem, workerThread: DbWorkerThread, db: ToDoItemDatabase?) {
            val task = Runnable { db?.ToDoItemDao()?.insert(item) }
            workerThread.postTask(task)
        }
    }
}