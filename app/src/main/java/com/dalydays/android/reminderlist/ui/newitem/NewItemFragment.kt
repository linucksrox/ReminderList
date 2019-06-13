package com.dalydays.android.reminderlist.ui.newitem

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.dalydays.android.reminderlist.R
import com.dalydays.android.reminderlist.databinding.FragmentNewItemBinding

class NewItemFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentNewItemBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_new_item, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = NewItemViewModelFactory(application)

        val newItemViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(NewItemViewModel::class.java)

        binding.viewmodel = newItemViewModel

        binding.lifecycleOwner = this

        // observe stuff here


        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_item_save_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
