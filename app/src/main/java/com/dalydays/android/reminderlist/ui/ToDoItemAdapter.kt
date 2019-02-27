package com.dalydays.android.reminderlist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.data.db.ToDoItem
import kotlinx.android.synthetic.main.checklist_item.view.*

class ToDoItemAdapter(val todoItemCheckedListener: OnTodoCheckedListener): RecyclerView.Adapter<ToDoItemAdapter.ViewHolder>() {

    private var toDoItems = emptyList<ToDoItem>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val checkbox: CheckBox = view.checkbox
        private val tvDescription: TextView = view.tv_description

        fun bind(position: Int) {
            checkbox.isChecked = toDoItems[position].checked
            checkbox.setOnClickListener {
                todoItemCheckedListener.onChecked(toDoItems[position].id)
            }
            tvDescription.text = toDoItems[position].description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.checklist_item, parent, false)
        return ViewHolder(itemView)
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
        fun onChecked(toDoItemId: Long?)
    }
}