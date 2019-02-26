package com.dalydays.android.reminderlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.ui.viewmodel.ChecklistViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_checklist.*
import kotlin.random.Random

class ChecklistFragment : Fragment() {

    private lateinit var checklistViewModel: ChecklistViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        items_list.layoutManager = LinearLayoutManager(activity)
        val adapter = ToDoItemAdapter(object: CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                Toast.makeText(activity, "checked", Toast.LENGTH_SHORT).show()
            }
        })
        items_list.adapter = adapter

        // Set up ViewModel
        checklistViewModel = ViewModelProviders.of(this).get(ChecklistViewModel::class.java)
        checklistViewModel.allToDoItems.observe(this, Observer { toDoItems ->
            toDoItems?.let { adapter.setToDoItems(it) }
        })

        // Set up fab
        add_list_item_fab.setOnClickListener {
            checklistViewModel.insert(ToDoItem(description = "Item ${Random.nextInt(1,999)}", checked = Random.nextBoolean()))
            // TODO("Navigate to the add item screen to get data for new todo item")
//            Navigation.createNavigateOnClickListener(R.id.action_checklist_to_new_item)
        }
    }
}
