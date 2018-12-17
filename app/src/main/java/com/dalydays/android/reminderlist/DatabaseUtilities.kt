package com.dalydays.android.reminderlist

import android.content.Context
import android.os.Handler
import android.widget.Toast
import com.dalydays.android.reminderlist.data.ToDoItem
import com.dalydays.android.reminderlist.data.ToDoItemDatabase

class DatabaseUtilities {
    companion object {
        fun insertToDoItem(item: ToDoItem, workerThread: DbWorkerThread, db: ToDoItemDatabase?) {
            val task = Runnable { db?.ToDoItemDao()?.insert(item) }
            workerThread.postTask(task)
        }

        fun fetchToDoItems(workerThread: DbWorkerThread, db: ToDoItemDatabase?, uiHandler: Handler, context: Context) {
            val task = Runnable {
                val allUsers = db?.ToDoItemDao()?.getAll()
                uiHandler.post {
                    if (allUsers == null || allUsers.isEmpty()) {
                        Toast.makeText(context, "No data found in db", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context, "Found ${allUsers.size} user(s) in the database", Toast.LENGTH_SHORT).show()
//                    bindDataWithUi(allUsers[0])
                    }
                }
            }
            workerThread.postTask(task)
        }
    }
}