package com.dalydays.android.reminderlist.ui.checklist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CheckListViewModelFactory(
        private val application: Application, private val deletedDescription: String) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChecklistViewModel::class.java)) {
            return ChecklistViewModel(application, deletedDescription) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}