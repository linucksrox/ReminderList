package com.dalydays.android.reminderlist.ui.edititem

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
import androidx.navigation.fragment.navArgs
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.databinding.FragmentEditItemBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditItemFragment : Fragment() {

    private lateinit var binding: FragmentEditItemBinding
    private lateinit var editItemViewModel: EditItemViewModel
    private lateinit var spinnerAdapter: ArrayAdapter<CharSequence>
    private val args: EditItemFragmentArgs by navArgs()

    // Create and inflate views and set up bindings
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_edit_item, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = EditItemViewModelFactory(application, args.itemId)

        editItemViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(EditItemViewModel::class.java)

        binding.viewmodel = editItemViewModel

        editItemViewModel.timeUnit.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.timeUnitSpinner.setSelection(spinnerAdapter.getPosition(it))
            }
        })

        editItemViewModel.saveItemEvent.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                saveAndReturn()
            }
        })

        editItemViewModel.description.observe(viewLifecycleOwner, Observer {
            editItemViewModel.validateInput()
        })

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        return binding.root
    }

    // Initialize values
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Populate time unit dropdown
        spinnerAdapter = ArrayAdapter.createFromResource(
                requireNotNull(context),
                R.array.time_units_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.timeUnitSpinner.adapter = adapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_item_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.delete -> {
            deleteAndReturn()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun deleteAndReturn() {
        closeSoftKeyboard()

        // dialog to confirm the user wants to deleteItem the item
        MaterialAlertDialogBuilder(context)
                .setTitle("Delete this item?")
                .setPositiveButton("Delete") { dialog, which ->
                    // if yes, proceed to deleteItem
                    editItemViewModel.deleteItem(
                            args.itemId,
                            binding.descriptionInput.text.toString(),
                            binding.switchRecurring.isChecked,
                            binding.durationInput.text.toString().toLong(),
                            binding.timeUnitSpinner.selectedItem.toString())
                    // navigate back to the main list
                    this.findNavController().navigate(EditItemFragmentDirections.actionEditItemToChecklist())
                }
                .setNegativeButton("Cancel", null)
                .show()
    }

    private fun saveAndReturn() {
        val description = binding.descriptionInput.text.toString()
        val recurring = binding.switchRecurring.isChecked
        val durationString = binding.durationInput.text.toString()
        val duration = when (durationString.isBlank()) {
            true -> 0L
            else -> durationString.toLong()
        }
        val timeUnit = binding.timeUnitSpinner.selectedItem.toString()
        // Save item to database
        editItemViewModel.saveItem(
                args.itemId,
                description,
                recurring,
                duration,
                timeUnit)

        closeSoftKeyboard()

        // go back!
        this.findNavController().navigate(EditItemFragmentDirections.actionEditItemToChecklist())
    }

    private fun closeSoftKeyboard() {
        val view = requireNotNull(view)
        view.let { v ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}
