package com.dalydays.android.reminderlist.ui.newitem

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewItemViewModelFactory(
        private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewItemViewModel::class.java)) {
            return NewItemViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}