package com.dalydays.android.reminderlist.data

import android.content.Context
import android.os.Handler
import android.widget.Toast
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.db.ToDoItemDatabase

class DatabaseUtilities {
    companion object {
        fun insertToDoItem(item: ToDoItem, workerThread: DbWorkerThread, db: ToDoItemDatabase?) {
            val task = Runnable { db?.ToDoItemDao()?.insert(item) }
            workerThread.postTask(task)
        }

        fun fetchToDoItems(workerThread: DbWorkerThread, db: ToDoItemDatabase?, uiHandler: Handler, context: Context) {
            val task = Runnable {
                val allItems = db?.ToDoItemDao()?.getAll()
                uiHandler.post {
                    if (allItems == null || allItems.isEmpty()) {
                        Toast.makeText(context, "No items found in db", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context, "Found ${allItems.size} items(s) in the database", Toast.LENGTH_SHORT).show()
//                    bindDataWithUi(allUsers[0])
                    }
                }
            }
            workerThread.postTask(task)
        }
    }
}