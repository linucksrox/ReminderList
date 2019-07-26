package com.dalydays.android.reminderlist.ui.checklist

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.databinding.FragmentChecklistBinding
import com.google.android.material.snackbar.Snackbar

class ChecklistFragment : Fragment() {

    private var backPressedCounter = 0
    private val backHandlerCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            backPressedCounter++

            when (backPressedCounter) {
                1 -> showSnackbarMessage("Press back again to quit")
                2 -> activity?.finish()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentChecklistBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_checklist, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = CheckListViewModelFactory(application)

        val checklistViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ChecklistViewModel::class.java)

        binding.checklistViewModel = checklistViewModel

        binding.lifecycleOwner = this

        binding.itemsList.layoutManager = LinearLayoutManager(activity)

        // set the plus icon color on the fab
        val fabIconColor =  ContextCompat.getColor(context!!, R.color.fab_color)
        val fabIconColorFilter = PorterDuffColorFilter(fabIconColor, PorterDuff.Mode.SRC_IN)
        binding.addListItemFab.drawable.colorFilter = fabIconColorFilter

        val adapter = ToDoItemAdapter { toDoItem ->
            checklistViewModel.toggleCheckbox(toDoItem)
        }

        binding.itemsList.adapter = adapter

        checklistViewModel.allToDoItems.observe(viewLifecycleOwner, Observer { allItems ->
            allItems?.let {
                adapter.submitList(allItems.sortedWith(compareBy(
                        {it.completed},
                        {it.description}))
                )
            }
        })

        checklistViewModel.navigateToNewToDoItem.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                // reset back handler and counter when navigating away from this fragment
                resetBackHandler()
                this.findNavController().navigate(ChecklistFragmentDirections.actionChecklistToNewItem())
            }
        })

        return binding.root
    }

    private fun resetBackHandler() {
        backPressedCounter = 0
        backHandlerCallback.isEnabled = false
    }

    private fun showSnackbarMessage(message: String) {
        val view = requireNotNull(view)
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        requireActivity().onBackPressedDispatcher.addCallback(backHandlerCallback)
    }

    override fun onResume() {
        super.onResume()
        // reset back button and enable custom back handler when starting or returning to this fragment
        backPressedCounter = 0
        backHandlerCallback.isEnabled = true
    }
}
