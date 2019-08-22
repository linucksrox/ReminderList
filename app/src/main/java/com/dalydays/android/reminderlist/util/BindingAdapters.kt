package com.dalydays.android.reminderlist.util

import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

@BindingAdapter("inputEnabled")
fun View.setInputEnabled(inputEnabled: Boolean?) {
    isEnabled = when (inputEnabled) {
        true -> true
        else -> false
    }
}

@BindingAdapter("duration", requireAll = false)
fun setDuration(view: EditText, duration: Long?) {
    view.setText(duration.toString())
}

@InverseBindingAdapter(attribute = "duration")
fun getDuration(view: EditText): Long {
    return when (view.editableText.isNullOrBlank()) {
        true -> 0
        else -> view.editableText.toString().toLong()
    }
}

@BindingAdapter("durationAttrChanged")
fun setListener(view: EditText, listener: InverseBindingListener) {
    view.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        listener.onChange()
    }
}