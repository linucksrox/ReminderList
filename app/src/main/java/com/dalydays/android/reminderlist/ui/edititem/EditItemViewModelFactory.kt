package com.dalydays.android.reminderlist.ui.edititem

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EditItemViewModelFactory(
        private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditItemViewModel::class.java)) {
            return EditItemViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}