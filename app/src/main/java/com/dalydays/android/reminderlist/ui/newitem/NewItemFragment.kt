package com.dalydays.android.reminderlist.ui.newitem

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.databinding.FragmentNewItemBinding
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

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        return binding.root
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

    private fun saveAndReturn() {
        // insert new ToDoItem into the database
        newItemViewModel.addNewItem(binding.descriptionInput.text.toString(),
                binding.switchRecurring.isChecked)

        // show a snackbar that the new item was saved
        val view = requireNotNull(view)
        Snackbar.make(view, "New todo item saved!", Snackbar.LENGTH_SHORT).show()

        // close the soft keyboard if it's open
        view.let { v ->
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }

        // go back!
        this.findNavController().navigate(NewItemFragmentDirections.actionNewItemToChecklist())
    }
}
