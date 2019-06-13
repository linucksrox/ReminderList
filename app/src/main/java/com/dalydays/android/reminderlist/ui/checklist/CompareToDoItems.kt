package com.dalydays.android.reminderlist.ui.checklist

import com.dalydays.android.reminderlist.data.db.ToDoItem

class CompareToDoItems {
    companion object: Comparator<ToDoItem> {
        override fun compare(o1: ToDoItem?, o2: ToDoItem?): Int = when {
            o1?.checked == true -> 1
            o1?.checked == false -> -1
            else -> 0
        }
    }
}