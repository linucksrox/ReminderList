package com.dalydays.android.reminderlist

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.dalydays.android.reminderlist.data.ToDoItem
import com.dalydays.android.reminderlist.data.ToDoItemDatabase
import kotlinx.android.synthetic.main.fragment_checklist.*

class Checklist : Fragment() {

    private val TAG: String = Checklist::class.java.simpleName

    private var mDb: ToDoItemDatabase? = null

    private lateinit var mDbWorkerThread: DbWorkerThread

    private val mUiHandler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_list_item_fab.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_checklist_to_newItem))

        mDbWorkerThread = DbWorkerThread("dbWorkerThread")
        mDbWorkerThread.start()

        mDb = ToDoItemDatabase.getDatabase(view.context)

        // Insert a test record
        Log.e(TAG, "Inserting test record")
        DatabaseUtilities.insertToDoItem(ToDoItem(description = "demo description", checked = true), mDbWorkerThread, mDb)

        // Refresh screen
        DatabaseUtilities.fetchToDoItems(mDbWorkerThread, mDb, mUiHandler, view.context)
    }

}
