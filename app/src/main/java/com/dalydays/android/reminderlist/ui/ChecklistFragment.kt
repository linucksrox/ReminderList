package com.dalydays.android.reminderlist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.ui.viewmodel.ChecklistViewModel
import kotlinx.android.synthetic.main.fragment_checklist.*

class ChecklistFragment : Fragment() {

    private lateinit var checklistViewModel: ChecklistViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Set up RecyclerView
        items_list.layoutManager = LinearLayoutManager(activity)
        val adapter = ToDoItemAdapter()
        items_list.adapter = adapter

        // Set up ViewModel
        checklistViewModel = ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
        checklistViewModel.allToDoItems.observe(this, Observer { toDoItems ->
            toDoItems?.let { adapter.setToDoItems(it) }
        })

        // Set up fab
        add_list_item_fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_checklist_to_newItem))

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist, container, false)
    }

}
