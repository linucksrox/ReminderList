package com.dalydays.android.reminderlist.ui.checklist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.data.db.ToDoItemDatabase
import com.dalydays.android.reminderlist.databinding.FragmentChecklistBinding
import com.google.android.material.snackbar.Snackbar

class ChecklistFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentChecklistBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_checklist, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = ToDoItemDatabase.getInstance(application).toDoItemDao

        val viewModelFactory = CheckListViewModelFactory(dataSource, application)

        val checklistViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ChecklistViewModel::class.java)

        binding.checklistViewModel = checklistViewModel

        binding.lifecycleOwner = this

        binding.itemsList.layoutManager = LinearLayoutManager(activity)

        val adapter = ToDoItemAdapter(object: ToDoClickCallback {
            override fun onClick(toDoItem: ToDoItem) {
                Snackbar.make(view!!, "clicked item ${toDoItem.id} with description ${toDoItem.description}", Snackbar.LENGTH_SHORT).show()
            }
        })

        binding.itemsList.adapter = adapter

        checklistViewModel.allToDoItems.observe(viewLifecycleOwner, Observer { allItems ->
            allItems?.let {
                adapter.submitList(allItems)
            }
        })

        checklistViewModel.addedActivityEvent.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                showSnackbarMessage(message)
                checklistViewModel.addedActivityEventComplete()
            }
        })

        checklistViewModel.checkedBoxEvent.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                showSnackbarMessage(message)
                checklistViewModel.checkedBoxEventComplete()
            }
        })

        return binding.root
    }

    private fun showSnackbarMessage(message: String) {
        val view = requireNotNull(view)
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
}
