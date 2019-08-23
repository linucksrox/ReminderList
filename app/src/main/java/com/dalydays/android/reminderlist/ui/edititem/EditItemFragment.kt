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

class EditItemFragment : Fragment() {

    private lateinit var binding: FragmentEditItemBinding
    private lateinit var newItemViewModel: EditItemViewModel
    private lateinit var spinnerAdapter: ArrayAdapter<CharSequence>
    private val args: EditItemFragmentArgs by navArgs()

    // Create and inflate views and set up bindings
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_edit_item, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = EditItemViewModelFactory(application, args.itemId)

        newItemViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(EditItemViewModel::class.java)

        binding.viewmodel = newItemViewModel

        newItemViewModel.timeUnit.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.timeUnitSpinner.setSelection(spinnerAdapter.getPosition(it))
            }
        })

        newItemViewModel.saveItem.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                saveAndReturn()
            }
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
        // dialog to confirm the user wants to deleteItem the item
        // TODO: make dialog here and check return type (true/false)

        // if yes, proceed to deleteItem
        newItemViewModel.deleteItem(
                args.itemId,
                binding.descriptionInput.text.toString(),
                binding.switchRecurring.isChecked,
                binding.durationInput.text.toString().toLong(),
                binding.timeUnitSpinner.selectedItem.toString())

        // navigate back to the main list
        this.findNavController().navigate(EditItemFragmentDirections.actionEditItemToChecklist())
    }

    private fun saveAndReturn() {
        // Save item to database
        newItemViewModel.saveItem(
                args.itemId,
                binding.descriptionInput.text.toString(),
                binding.switchRecurring.isChecked,
                binding.durationInput.text.toString().toLong(),
                binding.timeUnitSpinner.selectedItem.toString())

        // TODO: Validation should happen in the VM (or down the line) not here. So what we should do here is check the
        //  return value of saveItem() (which should return a status) and decide whether that worked and we can
        //  proceed to navigate back, or it failed so we should not navigate and highlight what's wrong on the form.

        // close the soft keyboard if it's open
        val view = requireNotNull(view)
        view.let { v ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }

        // go back!
        this.findNavController().navigate(EditItemFragmentDirections.actionEditItemToChecklist())
    }
}
