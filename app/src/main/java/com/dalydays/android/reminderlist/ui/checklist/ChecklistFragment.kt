package com.dalydays.android.reminderlist.ui.checklist

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.data.db.ToDoItem
import com.dalydays.android.reminderlist.databinding.FragmentChecklistBinding
import com.google.android.material.snackbar.Snackbar

class ChecklistFragment : Fragment() {

    private val args: ChecklistFragmentArgs by navArgs()
    private var backPressedCounter = 0
    private val backHandlerCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            when (++backPressedCounter) {
                1 -> showSnackBarMessage("Press back again to quit")
                2 -> activity?.finish()
            }
        }
    }

    private lateinit var adapter: ToDoItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentChecklistBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_checklist, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = CheckListViewModelFactory(application, args.deletedDescription)

        val checklistViewModel = ViewModelProvider(this, viewModelFactory)
                .get(ChecklistViewModel::class.java)

        binding.checklistViewModel = checklistViewModel

        binding.lifecycleOwner = this

        binding.itemsList.layoutManager = LinearLayoutManager(activity)

        val onCheckboxClickHandler: (ToDoItem, Int) -> Unit = { toDoItem, position ->
            checklistViewModel.toggleCheckbox(toDoItem)
            adapter.notifyItemChanged(position)
        }

        val onCardClickHandler: (ToDoItem) -> Unit = { toDoItem ->
            resetBackHandler()
            val itemId = toDoItem.id ?: -1L
            this.findNavController().navigate(ChecklistFragmentDirections.actionChecklistToEditItem(itemId))
        }

        val fragmentContext = requireNotNull(context)
        val workManager = WorkManager.getInstance(fragmentContext)

        adapter = ToDoItemAdapter(onCheckboxClickHandler, onCardClickHandler, workManager, this)

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
                this.findNavController().navigate(ChecklistFragmentDirections.actionChecklistToEditItem())
            }
        })

        checklistViewModel.showDeletedSnackBar.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { message ->
                val displayMessage = getString(R.string.deleted_notification, message)
                showSnackBarMessage(displayMessage)
            }
        })

        checklistViewModel.showScheduledSnackBar.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { message ->
                val displayMessage = getString(R.string.scheduled_notification, message)
                showSnackBarMessage(displayMessage)
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.checklist_item_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.about -> {
            // Go to about page
//            this.findNavController().navigate(EditItemFragmentDirections.actionEditItemToChecklist())
            this.findNavController().navigate(ChecklistFragmentDirections.actionChecklistToAbout())
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun resetBackHandler() {
        backPressedCounter = 0
        backHandlerCallback.isEnabled = false
    }

    private fun showSnackBarMessage(message: String) {
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

        // close soft keyboard if it was open
        closeSoftKeyboard()
    }

    private fun closeSoftKeyboard() {
        val view = requireNotNull(view)
        view.let { v ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}
