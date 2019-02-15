package com.dalydays.android.reminderlist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.data.db.ToDoItem
import kotlinx.android.synthetic.main.checklist_item.view.*

class ToDoItemAdapter : RecyclerView.Adapter<ToDoItemAdapter.ViewHolder>() {

    private var toDoItems = emptyList<ToDoItem>()

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val checkbox = view.checkbox
        val tvDescription = view.tv_description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.checklist_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkbox.isChecked = toDoItems[position].checked
        holder.tvDescription.text = toDoItems[position].description
    }

    override fun getItemCount() = toDoItems.size

    internal fun setToDoItems(toDoItems: List<ToDoItem>) {
        this.toDoItems = toDoItems
        notifyDataSetChanged()
    }
}