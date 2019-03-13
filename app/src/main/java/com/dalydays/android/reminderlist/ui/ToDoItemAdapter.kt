package com.dalydays.android.reminderlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.databinding.ChecklistItemBinding

class ToDoItemAdapter(val todoItemCheckedListener: OnTodoCheckedListener): RecyclerView.Adapter<ToDoItemAdapter.ViewHolder>() {

    private var toDoItems = emptyList<ToDoItem>()

    inner class ViewHolder(private val binding: ChecklistItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.todoItem = toDoItems[position]
            binding.executePendingBindings()
            //TODO("figure out how to set this click listener up with the data binding")
//            checkbox.setOnClickListener {
//                val updatedToDoItem = toDoItems[position]
//                updatedToDoItem.checked = checkbox.isChecked
//                todoItemCheckedListener.onChecked(updatedToDoItem)
//            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChecklistItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = toDoItems.size

    internal fun setToDoItems(toDoItems: List<ToDoItem>) {
        this.toDoItems = toDoItems
        // TODO("this should be changed to specific notify... method for performance and to allow animations to work properly")
        // But how do you know which "position" to update when doing something like notifyItemInserted()?
        notifyDataSetChanged()
    }

    interface OnTodoCheckedListener {
        fun onChecked(toDoItem: ToDoItem)
    }
}