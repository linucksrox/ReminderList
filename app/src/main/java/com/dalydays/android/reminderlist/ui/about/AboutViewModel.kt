package com.dalydays.android.reminderlist.ui.about

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel

class AboutViewModel(application: Application) : AndroidViewModel(application) {
    fun onGitHubLinkClicked(context: Context) {
        // new intent to open the URL in the browser
        val webpage: Uri = Uri.parse("https://github.com/linucksrox/ReminderList")
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        val packageManager = context.packageManager
        if (intent.resolveActivity(packageManager) != null) {
            context.startActivity(intent)
        }
    }
}