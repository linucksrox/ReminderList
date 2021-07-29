package com.dalydays.android.reminderlist.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dalydays.android.reminderlist.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    private lateinit var aboutViewModel: AboutViewModel
    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentAboutBinding.inflate(layoutInflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        val viewModelFactory = AboutViewModelFactory(application)

        aboutViewModel = ViewModelProvider(this, viewModelFactory)
                .get(AboutViewModel::class.java)

        binding.githubLink.setOnClickListener {
            aboutViewModel.onGitHubLinkClicked(requireNotNull(context), view)
        }

        return view
    }

}
