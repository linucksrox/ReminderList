package com.dalydays.android.reminderlist.ui.checklist

import com.dalydays.android.reminderlist.data.db.ToDoItem

interface ToDoClickCallback {
    fun onClick(toDoItem: ToDoItem)
}