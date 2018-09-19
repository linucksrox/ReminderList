package com.dalydays.android.reminderlist


import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.fragment_checklist.*

class Checklist : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab.setOnClickListener { addNewItemDialog() }
    }

    private fun addNewItemDialog() {
        val alert = AlertDialog.Builder(context)

        val itemEditText = EditText(context)
        alert.setTitle("Enter To Do Item Text")

        alert.setView(itemEditText)

        alert.setPositiveButton("Save") {_, _ -> }

        alert.show()
    }

}
