package com.dalydays.android.reminderlist.ui.about

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.AndroidViewModel
import com.google.android.material.snackbar.Snackbar

class AboutViewModel(application: Application) : AndroidViewModel(application) {
    fun onGitHubLinkClicked(context: Context, view: View) {
        // new intent to open the URL in the browser
        val webpage: Uri = Uri.parse("https://github.com/linucksrox/ReminderList")
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        try {
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Snackbar.make(context, view, "Unable to open link", Snackbar.LENGTH_LONG).show()
        }
    }
}