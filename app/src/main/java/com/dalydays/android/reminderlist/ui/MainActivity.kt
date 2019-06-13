package com.dalydays.android.reminderlist.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.dalydays.android.reminderlist.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)

        val navigationView = findViewById<NavigationView>(R.id.nav_graph)
        if (navigationView == null) {
            menuInflater.inflate(R.menu.main_menu, menu)
            return true
        }

        return retValue
    }
}
