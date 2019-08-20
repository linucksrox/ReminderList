package com.dalydays.android.reminderlist.ui.edititem

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditItemViewModelFactory(
        private val application: Application,
        private val itemId: Long) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditItemViewModel::class.java)) {
            return EditItemViewModel(application, itemId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}