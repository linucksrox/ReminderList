package com.dalydays.android.reminderlist.util

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("inputEnabled")
fun View.setInputEnabled(inputEnabled: Boolean?) {
    isEnabled = when (inputEnabled) {
        true -> true
        else -> false
    }
}