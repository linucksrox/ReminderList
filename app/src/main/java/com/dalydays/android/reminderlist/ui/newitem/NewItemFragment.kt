package com.dalydays.android.reminderlist.ui.newitem

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.databinding.FragmentNewItemBinding
import com.dalydays.android.reminderlist.util.Schedule
import com.google.android.material.snackbar.Snackbar

class NewItemFragment : Fragment() {

    private lateinit var binding: FragmentNewItemBinding
    private lateinit var newItemViewModel: NewItemViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_new_item, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = NewItemViewModelFactory(application)

        newItemViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewItemViewModel::class.java)

        binding.viewmodel = newItemViewModel

        // Populate time unit dropdown
        ArrayAdapter.createFromResource(
                context,
                R.array.time_units_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.timeUnitSpinner.adapter = adapter
        }

        // Handle enable/disable input views when scheduling is toggled on or off
        newItemViewModel.toggleScheduleEvent.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                toggleEnableInputs()
            }
        })

        // Default inputs enabled/disabled based on default state of schedule toggle defined in the layout
        toggleEnableInputs()

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun toggleEnableInputs() {
        if (binding.switchRecurring.isChecked) {
            binding.timeInput.isEnabled = true
            binding.timeUnitSpinner.isEnabled = true
        } else {
            binding.timeInput.isEnabled = false
            binding.timeUnitSpinner.isEnabled = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_item_save_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.save -> {
            saveAndReturn()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun toggleEnabledForInputViews() {

    }

    private fun saveAndReturn() {
        // insert new ToDoItem into the database
        newItemViewModel.addNewItem(
                binding.descriptionInput.text.toString(),
                binding.switchRecurring.isChecked,
                binding.timeInput.text.toString().toLong(),
                binding.timeUnitSpinner.selectedItem.toString())

        val time = binding.timeInput.text.toString()
        val timeUnit = binding.timeUnitSpinner.selectedItem.toString()
        val schedule = Schedule.build(time.toLong(), timeUnit)

        // show a snackbar that the new item was saved
        val view = requireNotNull(view)
//        Snackbar.make(view, "schedule duration: ${schedule.duration}, schedule timeUnit: ${schedule.getTimeUnitAsString()}", Snackbar.LENGTH_LONG).show()
        Snackbar.make(view, "storing values duration: $time, timeUnit: $timeUnit", Snackbar.LENGTH_LONG).show()

        // close the soft keyboard if it's open
        view.let { v ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }

        // go back!
        this.findNavController().navigate(NewItemFragmentDirections.actionNewItemToChecklist())
    }

}
