package com.dalydays.android.reminderlist

class ToDoItem {

    companion object {
        fun create(): ToDoItem = ToDoItem()
    }

    var objectId: String? = null
    var itemText: String? = null
    var done: Boolean? = false
}