package com.dalydays.android.reminderlist.ui.checklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.databinding.ChecklistItemBinding

class ToDoItemAdapter(private val onCheckboxClick: (ToDoItem) -> Unit, private val onCardClick: (ToDoItem) -> Unit): ListAdapter<ToDoItem, RecyclerView.ViewHolder>(ToDoItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val toDoItem = getItem(position)
                holder.bind(toDoItem, onCheckboxClick, onCardClick)
            }
        }
    }

    class ViewHolder private constructor(private val binding: ChecklistItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(toDoItem: ToDoItem, onCheckboxClick: (ToDoItem) -> Unit, onCardClick: (ToDoItem) -> Unit) {
            binding.todoItem = toDoItem
            binding.checkboxCompleted.setOnClickListener {
                onCheckboxClick(toDoItem)
            }
            binding.cardTodo.setOnClickListener {
                onCardClick(toDoItem)
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ChecklistItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ToDoItemDiffCallback : DiffUtil.ItemCallback<ToDoItem>() {
    override fun areItemsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
        return oldItem == newItem
    }
}
