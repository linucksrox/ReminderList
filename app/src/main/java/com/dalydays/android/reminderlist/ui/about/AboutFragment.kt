package com.dalydays.android.reminderlist.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dalydays.android.reminderlist.R
import kotlinx.android.synthetic.main.fragment_about.view.*

class AboutFragment : Fragment() {

    private lateinit var aboutViewModel: AboutViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        val application = requireNotNull(this.activity).application
        val viewModelFactory = AboutViewModelFactory(application)

        aboutViewModel = ViewModelProvider(this, viewModelFactory)
                .get(AboutViewModel::class.java)

        view.github_link.setOnClickListener {
            aboutViewModel.onGitHubLinkClicked(requireNotNull(context))
        }

        return view
    }

}
