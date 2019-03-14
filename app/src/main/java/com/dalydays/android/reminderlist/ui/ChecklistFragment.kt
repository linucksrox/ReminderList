package com.dalydays.android.reminderlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dalydays.android.reminderlist.databinding.FragmentChecklistBinding
import com.dalydays.android.reminderlist.ui.viewmodel.ChecklistViewModel
import kotlinx.android.synthetic.main.fragment_checklist.*

class ChecklistFragment : Fragment() {

    private lateinit var checklistViewModel: ChecklistViewModel
    private lateinit var binding: FragmentChecklistBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        binding = FragmentChecklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up ViewModel and RecyclerView
        checklistViewModel = ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
        val adapter = ToDoItemAdapter(checklistViewModel)
        checklistViewModel.allToDoItems.observe(this, Observer { toDoItems ->
            toDoItems?.let { adapter.setToDoItems(it) }
        })
        items_list.layoutManager = LinearLayoutManager(activity)
        items_list.adapter = adapter

        // Set up fab
        binding.viewmodel = checklistViewModel
    }
}
