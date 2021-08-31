package com.alperen.moviebox.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.alperen.moviebox.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val menu = findViewById<BottomNavigationView>(R.id.menu)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        menu.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        makeAppStopped()
    }

    private fun makeAppStopped() {
        val sharedPref = this.getSharedPreferences("", Context.MODE_PRIVATE)
        val editor = sharedPref?.edit()
        editor?.putBoolean("isAppStarted", false)
        editor?.apply()
    }
}